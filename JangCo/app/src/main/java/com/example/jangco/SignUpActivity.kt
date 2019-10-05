package com.example.jangco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    private val SEARCH_ADDRESS_ACTIVITY: Int = 10000

    val SIGNUP_BASIC_INFO: Int = 1
    val SIGNUP_SCHOOL_GRADE_INFO: Int = 2

    var majorData: Array<String>? = null
    var schoolData:HashMap<String,ArrayList<String>> = HashMap<String,ArrayList<String>>()
    var schoolDataKeys: MutableSet<String>? = null
    private var fragmentManager = supportFragmentManager
    var signUpBasicInfoFragment = SignUpBasicInfoFragment()
    var signUpSchoolGradeInfoFragment = SignUpSchoolGradeInfoFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        settingToolBar()
        settingFragment(SIGNUP_BASIC_INFO)
        readSchoolData()
    }

    fun settingFragment(fragmentCode: Int) {
        var tran = fragmentManager.beginTransaction()
        when(fragmentCode){
            SIGNUP_BASIC_INFO -> {
                tran.replace(R.id.signUpActivity_frame_layout, signUpBasicInfoFragment)
            }
            SIGNUP_SCHOOL_GRADE_INFO -> {
                tran.replace(R.id.signUpActivity_frame_layout, signUpSchoolGradeInfoFragment)
                tran.addToBackStack(null)
            }
        }
        tran.commit()
    }

    // signUpSchoolGradeInfo Fragment 에서 사용할 데이터를 담아온다.
    fun readSchoolData() {
        var cvsReader = CvsReader()
        schoolData = cvsReader.readCsvFile(resources.openRawResource(R.raw.colleage_data_2019))
        // schoolData.keys 는  추상 클래스 MutableSet을 반환 하기 때문에 add addAll 등 원소 추가 불가능하다
        schoolDataKeys = schoolData.keys.toMutableSet()
        var tempList = schoolDataKeys?.sorted()
        schoolDataKeys =tempList?.toSortedSet()


        //자동 검색 결과 sort schoolDataKeys?.toSortedSet()
        //Toast.makeText(this,"readSchoolData Load Complete \n ${schoolDataKeys!!.size}개의 대학" ,Toast.LENGTH_LONG).show()
        //Log.d("readSchoolData", schoolDataKeys!!.elementAt(1861))

    }

    private fun settingToolBar(){
        setSupportActionBar(signUpActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게

        var toolbar = layoutInflater.inflate(R.layout.activity_sign_up_toolbar, null)
        supportActionBar?.customView = toolbar

        var backButton = toolbar.findViewById<ImageView>(R.id.toolbarBackImageView)
        backButton.setOnClickListener{ view ->
            finish()
        }

    }

    fun startSearchAddressActivity(){
        val i = Intent(this, SearchAddressActivity::class.java)
        startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SEARCH_ADDRESS_ACTIVITY ->
                if (resultCode == RESULT_OK) {
                    val data = data?.extras!!.getString("data")
                    if (data != null) {
                        var address = data
                        var addressList: List<String>? = address.split(" ")
                        var addressSize = addressList?.size
                        signUpBasicInfoFragment.cityProvince?.visibility = View.VISIBLE
                        signUpBasicInfoFragment.district?.visibility = View.VISIBLE
                        signUpBasicInfoFragment.detailAddress?.visibility = View.VISIBLE
                        signUpBasicInfoFragment.nextStepButton?.visibility = View.VISIBLE

                        for( i in 0..addressSize!!-1!!){
                            if(i == 0)
                                signUpBasicInfoFragment.detailAddress?.setText(addressList?.get(0))
                            else if(i == 1)
                                signUpBasicInfoFragment.cityProvince?.setText(addressList?.get(1))
                            else if(i == 2)
                                signUpBasicInfoFragment.district?.setText(addressList?.get(2))
                            else
                                signUpBasicInfoFragment.detailAddress?.append(addressList?.get(i)+" ")
                        }
                    }else{
                        Toast.makeText(this, R.string.nullAddressAlert, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

}
