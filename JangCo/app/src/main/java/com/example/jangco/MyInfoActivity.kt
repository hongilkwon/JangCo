package com.example.jangco

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_profile.*
import kotlinx.android.synthetic.main.activity_my_info.*
import java.util.*
import kotlin.collections.HashMap

class MyInfoActivity : AppCompatActivity(), View.OnClickListener {

    private val CHANGE_PROFILE_ACTIVITY = 1
    private val CHANGE_ADDRESS_ACTIVITY = 2
    private val CHANGE_SCHOOL_ACTIVITY = 3
    private val CHANGE_GRADE_ACTIVITY  = 4
    private val CHANGE_INCOME_ACTIVITY = 5
    private val CHANGE_SQUALIFICATION_ACTIVITY = 6

    var userAllInfo: HashMap<String, Any>? = null
    var userProfile: User? = null
    var userAddress: Address? = null
    var userSchool: School? = null
    var userGrade: Grade? = null
    var userIncome: Income? =  null
    var userSQualification: SQualification? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)
        settingToolBar()
        userAllInfo = intent.getSerializableExtra("userAllInfo") as HashMap<String, Any>

        for (data in userAllInfo!!) {
            var obj = data.value
            if (obj is User) {
                userProfile = obj
            } else if (obj is Address) {
                userAddress = obj
            } else if (obj is School) {
                userSchool = obj
            } else if (obj is Grade) {
                userGrade = obj
            }
            if (obj is Income) {
                userIncome = obj
            }
            if (obj is SQualification) {
                userSQualification = obj
            }
        }
        settingUserInfo()

        // 리스너 설정.
        myInfoActivityProfileChangeButton.setOnClickListener(this)
        myInfoActivityChangePwImageView.setOnClickListener(this)
        myInfoActivityChangeAddressImageView.setOnClickListener(this)
        myInfoActivityChangeSchoolImageView.setOnClickListener(this)
        myInfoActivityChangeGradeImageView.setOnClickListener(this)
        myInfoActivityChangeIncomeImageView.setOnClickListener(this)
        myInfoActivityChangeSQualificationImageView.setOnClickListener(this)
    }

    private fun settingUserInfo() {
        val requestOptions = RequestOptions()
            // .skipMemoryCache(true)    //캐시 사용 해제, Firebase 사용 시 느리기 때문에 사용 필수
            // .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_loading) // 로딩시.
            .error(R.drawable.ic_default_user_profile_256dp) // 이미지 없을때.
        Glide.with(this)
            .load(userProfile?.proFileImageUri)
            .apply(requestOptions)
            .thumbnail(0.5f)
            .into(myInfoActivityProfileCircleVIew)

        myInfoActivityNickNameTextView.text = userProfile?.nickName
        myInfoActivityEmailTextView.text = userProfile?.id
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.myInfoActivityProfileChangeButton -> {
                var intent = Intent(baseContext, ChangeProfileActivity::class.java)
                intent.putExtra("userProfile",userProfile)
                startActivityForResult(intent,CHANGE_PROFILE_ACTIVITY)
            }
            R.id.myInfoActivityChangePwImageView -> {
                var intent = Intent(baseContext, ChangePwActivity::class.java)
                startActivity(intent)
            }
            R.id.myInfoActivityChangeAddressImageView -> {
                var intent = Intent(baseContext, ChangeAddressActivity::class.java)
                intent.putExtra("userAddress",userAddress)
                startActivityForResult(intent,CHANGE_ADDRESS_ACTIVITY)
            }
            R.id.myInfoActivityChangeSchoolImageView -> {
                var intent = Intent(baseContext, ChangeSchoolActivity::class.java)
                intent.putExtra("userSchool", userSchool)
                startActivityForResult(intent,CHANGE_SCHOOL_ACTIVITY)
            }
            R.id.myInfoActivityChangeGradeImageView -> {
                var intent = Intent(baseContext, ChangeGradeActivity::class.java)
                intent.putExtra("userGrade", userGrade)
                startActivityForResult(intent,CHANGE_GRADE_ACTIVITY)
            }
            R.id.myInfoActivityChangeIncomeImageView -> {
                var intent = Intent(baseContext, ChangeIncomeActivity::class.java)
                intent.putExtra("userIncome", userIncome)
                startActivityForResult(intent,CHANGE_INCOME_ACTIVITY)
            }
            R.id.myInfoActivityChangeSQualificationImageView -> {
                var intent = Intent(baseContext, ChangeSQualificationActivity::class.java)
                intent.putExtra("userSQualification", userSQualification)
                startActivityForResult(intent,CHANGE_SQUALIFICATION_ACTIVITY)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CHANGE_PROFILE_ACTIVITY -> {
                if(resultCode == RESULT_OK) {
                    //프로필 정보 갱신
                    userProfile = data?.getSerializableExtra("userProfile") as User
                    myInfoActivityNickNameTextView.text = userProfile?.nickName

                    val requestOptions = RequestOptions()
                        // .skipMemoryCache(true)    //캐시 사용 해제, Firebase 사용 시 느리기 때문에 사용 필수
                        // .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.ic_loading) // 로딩시.
                        .error(R.drawable.ic_default_user_profile_256dp) // 이미지 없을때.

                    Glide.with(this)
                        .load(userProfile?.proFileImageUri)
                        .apply(requestOptions)
                        .thumbnail(0.5f)
                        .into(myInfoActivityProfileCircleVIew)
                    //모든 정보에서  프로필정보 갱신
                    userAllInfo?.put("user", userProfile as Any)
                }
            }
            CHANGE_ADDRESS_ACTIVITY -> {
                if(resultCode == RESULT_OK) {
                    userAddress = data?.getSerializableExtra("userAddress") as Address
                    userAllInfo?.put("address", userAddress as Any)
                }
            }
            CHANGE_SCHOOL_ACTIVITY -> {
                if(resultCode == RESULT_OK) {
                    userSchool = data?.getSerializableExtra("userSchool") as School
                    userAllInfo?.put("school", userSchool as Any)
                }
            }
            CHANGE_GRADE_ACTIVITY -> {
                if(resultCode == RESULT_OK) {
                    userGrade = data?.getSerializableExtra("userGrade") as Grade
                    userAllInfo?.put("grade", userGrade as Any)
                }
            }
            CHANGE_INCOME_ACTIVITY -> {
                if(resultCode == RESULT_OK) {
                    userIncome = data?.getSerializableExtra("userIncome") as Income
                    userAllInfo?.put("income", userIncome as Any)
                }
            }
            CHANGE_SQUALIFICATION_ACTIVITY -> {
                if(resultCode == RESULT_OK) {
                    userSQualification = data?.getSerializableExtra("userSQualification") as SQualification
                    userAllInfo?.put("sQualification", userGrade as Any)
                }
            }
        }
    }

    private fun settingToolBar(){
        setSupportActionBar(myInfoActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게

        var toolbar = layoutInflater.inflate(R.layout.activity_myinfo_toolbar, null)
        supportActionBar?.customView = toolbar

        var backButton = toolbar.findViewById<ImageView>(R.id.myInfoToolbarBackImageView)
        backButton.setOnClickListener{ view ->
            intent.putExtra("userAllInfo", userAllInfo)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        intent.putExtra("userAllInfo", userAllInfo)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
