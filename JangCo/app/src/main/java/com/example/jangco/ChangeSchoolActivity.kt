package com.example.jangco

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_school.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeSchoolActivity : AppCompatActivity(), View.OnClickListener {

    private var recyclerViewAdapter: SchoolRecyclerViewAdapter?=null
    var spinnerAdapter: ArrayAdapter<String>? = null

    var schoolData:HashMap<String,ArrayList<String>> = HashMap<String,ArrayList<String>>()
    var schoolDataKeys: MutableSet<String>? = null
    var majorMap = HashMap<String,String>()

    private var userSchool: School? = null


    var status: String? = null

    private lateinit var dataBaseHelper: DataBaseHelper
    private val firebaseAuth = FirebaseAuth.getInstance()

    lateinit var loadingDialog: CustomLoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_school)
        settingToolBar()
        readSchoolData()

        loadingDialog = CustomLoadingDialog(CustomLoadingDialog.DATA_LOADING_DIALOG_TYPE,this)
        loadingDialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        userSchool = intent.getSerializableExtra("userSchool") as School
        dataBaseHelper = DataBaseHelper(firebaseAuth.currentUser?.email!!)

        // schoolRecyclerView recyclerViewAdapter 참조 및 어뎁터 설정.
        // 어뎁터 에 들어갈.
        recyclerViewAdapter = schoolDataKeys?.let { SchoolRecyclerViewAdapter(it, this) }
        changeSchoolActivityRecyclerView.adapter =recyclerViewAdapter
        changeSchoolActivityRecyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                baseContext, changeSchoolActivityRecyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // view는 item layout이며 position은 현재 recyclerView내의 item의 위치이다.
                        var selectedName =  view.findViewById<TextView>(R.id.school).text.toString()

                        changeSchoolActivitySchoolNameTextView?.text = selectedName
                        // 학교이름창 및 전공선택 안보이게
                        if(changeSchoolActivitySchoolLayout.visibility == View.INVISIBLE)
                            changeSchoolActivitySchoolLayout.visibility = View.VISIBLE
                        // Recyclerview item 클릭시 List안보이게.
                        if(changeSchoolActivityRecyclerView.visibility == View.VISIBLE)
                            changeSchoolActivityRecyclerView.visibility= View.GONE
                        settingSpinner()
                        closeKeyboard()
                    }
                })
        )
        // 학교검색창(schoolSearchEditText) 의 참조 및 textWatcher 설정.
        changeSchoolActivitySearchEditText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 학교이름창 및 전공선택
                if(changeSchoolActivitySchoolLayout.visibility == View.VISIBLE)
                    changeSchoolActivitySchoolLayout.visibility= View.INVISIBLE
                // RecyclerView 보이게 하기.
                if(changeSchoolActivityRecyclerView.visibility == View.GONE)
                    changeSchoolActivityRecyclerView.visibility= View.VISIBLE

                recyclerViewAdapter?.searchFilter(s.toString())
                recyclerViewAdapter?.notifyDataSetChanged()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        status = getString(R.string.searchForSchoolAttending)
        changeSchoolActivityStatusRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.Attending -> {status = getString(R.string.searchForSchoolAttending)}
                R.id.Absence -> { status = getString(R.string.searchForSchoolAbsence)}
                R.id.Graduated -> {status = getString(R.string.searchForSchoolGraduated)}
            }
        }
        changeSchoolActivityCancelButton.setOnClickListener(this)
        changeSchoolActivityUpdateButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.changeSchoolActivityCancelButton -> {
                finish()
            }
            R.id.changeSchoolActivityUpdateButton -> {

                if(inspectionSchool() && inspectionMajorSpinner(changeSchoolActivityMajorSelectSpinner) && inspectionStatus(status!!)){
                    loadingDialog.show()
                    CoroutineScope(Dispatchers.IO).launch {
                        // 순서 반드시 지켜야됨 -> 나중에 더 효율적으로 변경 예정.
                        // 학교 객체 수정.
                        userSchool?.name = arrayListOf(changeSchoolActivitySchoolNameTextView.text.toString())

                        var selectedItem =
                            changeSchoolActivityMajorSelectSpinner?.selectedItem.toString()
                        var selectedMajor = selectedItem.split(" ")
                        var etcInfo = majorMap[selectedItem]?.split("*")

                        userSchool?.major = arrayListOf(selectedMajor[0])
                        userSchool?.track = arrayListOf(selectedMajor[1])

                        userSchool?.collageDivision = arrayListOf(etcInfo?.get(0)!!)
////                        userSchool?.establishDivision = etcInfo?.get(1)
////                        userSchool?.areaDivision = etcInfo?.get(2)
//                        userSchool?.period = etcInfo?.get(3)
                        userSchool?.status = arrayListOf(status!!)
                        dataBaseHelper.upDateUserSchool(userSchool!!)

                        intent.putExtra("userSchool", userSchool)
                        setResult(RESULT_OK, intent)
                        loadingDialog.dismiss()
                        finish()
                    }
                }
            }
        }
    }
    // 학적상태 검사.
    private fun inspectionStatus(status: String):Boolean{
        if(status.isNullOrEmpty()){
            Toast.makeText(this,R.string.statusSelectInspection, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    //학교데이터 검사.
    private fun inspectionSchool(): Boolean{
        if (changeSchoolActivitySchoolNameTextView.text.toString().isNullOrEmpty()) {
            Toast.makeText(this,R.string.schoolEmptyInspection, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    // 전공선택 스피너 검사.
    fun inspectionMajorSpinner(spinner: Spinner?):Boolean{
        var major = spinner?.selectedItem as String
        if(major.equals(this.getString(R.string.MajorSpinnerTitle)) || major.isEmpty()){
            Toast.makeText(this,R.string.majorSelectInspection, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun readSchoolData() {
        var cvsReader = CvsReader()
        schoolData = cvsReader.readCsvFile(resources.openRawResource(R.raw.colleage_data_2019))
        // schoolData.keys 는  추상 클래스 MutableSet을 반환 하기 때문에 add addAll 등 원소 추가 불가능하다
        schoolDataKeys = schoolData.keys.toMutableSet()
        var tempList = schoolDataKeys?.sorted()
        schoolDataKeys =tempList?.toSortedSet()
    }

    private fun settingToolBar(){
        setSupportActionBar(changeSchoolActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게

        var toolbar = layoutInflater.inflate(R.layout.activity_change_toolbar, null)
        supportActionBar?.customView = toolbar

        var title = toolbar.findViewById<TextView>(R.id.changeToolbarTitleTextView)
        title.text = getString(R.string.moreInfoFragmentMyInfoSchoolChange)

        var closeButton = toolbar.findViewById<ImageView>(R.id.changeToolbarBackImageView)
        closeButton.setOnClickListener { view ->
            finish()
        }
    }

    //소프트 키보드 강제로 내리기.
    fun closeKeyboard(){
        var view = this.currentFocus
        if(view != null){
            val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    // 학교정보가 정해지면 학과정보를 정해서 스피너 셋팅.
    fun settingSpinner(){
        if(changeSchoolActivitySchoolNameTextView.text.toString().isNotEmpty()) {
            var majorData = this.schoolData?.get(changeSchoolActivitySchoolNameTextView.text.toString())!!
            // 학과 계열 : 기타로  해쉬맵생성.
            for( item in majorData!!){
                var tempList=item.split("*")
//               majorMap.put(tempList[0]+" "+tempList[1],tempList[2]+"*"+tempList[3]+"*"+tempList[4]+"*"+tempList[5])
                majorMap.put(tempList[0]+" "+tempList[1],tempList[2])
            }
            // 해쉬맵에서 키만 빼서 리스트로 생성, 정렬
            var spinnerData = majorMap.keys.toMutableList()
            spinnerData?.sort()
            // 셋팅.
            val spinnerTitle = this.getString(R.string.MajorSpinnerTitle)
            spinnerData.add(0, spinnerTitle)
            spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerData)
            spinnerAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            changeSchoolActivityMajorSelectSpinner.adapter = spinnerAdapter
        }
    }

}
