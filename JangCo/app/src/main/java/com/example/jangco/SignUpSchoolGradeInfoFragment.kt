package com.example.jangco


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import java.util.regex.Pattern


class SignUpSchoolGradeInfoFragment : Fragment(), View.OnClickListener {

    var signUpActivity: SignUpActivity? = null
    // recyclerView
    var schoolRecyclerView: RecyclerView? = null
    private var recyclerViewAdapter: SchoolRecyclerViewAdapter?=null
    //검색창
    var schoolSearchEditText: EditText? = null
    var schoolName: TextView? = null
    // 스피너
    var majorSelectSpinner: Spinner? = null
    var spinnerAdapter: ArrayAdapter<String>? = null
    // 성적.
    var signUpSchoolGradeTotalAverage: EditText? = null
    var signUpSchoolGradeTotalPercentage: EditText? = null
    var signUpSchoolGradeLastAverage: EditText? = null
    var signUpSchoolGradeLastPercentage: EditText? = null
    // 완료 버튼.
    var signUpCompleteButton: Button? = null
    // 성적 레이아웃.
    var gradeLayout: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // signUpActivity 참조.
        signUpActivity = activity as SignUpActivity
        signUpActivity?.readSchoolData()
        val view = inflater.inflate(R.layout.fragment_sign_up_school_grade_info, container, false)
        // 학교이름 textView 참조
        schoolName = view.findViewById(R.id.signUpGradeInfoSchoolNameTextView)
        // Spinner 참조.
        majorSelectSpinner = view.findViewById(R.id.signUpSchoolGradeInfoMajorSelectSpinner)

       // schoolRecyclerView recyclerViewAdapter 참조 및 어뎁터 설정.
        schoolRecyclerView = view.findViewById<RecyclerView?>(R.id.signUpSchoolGradeInfoRecyclerView)
        recyclerViewAdapter = signUpActivity?.schoolDataKeys?.let { SchoolRecyclerViewAdapter(it, context) }
        schoolRecyclerView?.adapter =recyclerViewAdapter
        schoolRecyclerView?.addOnItemTouchListener(
            RecyclerItemClickListener(
                signUpActivity!!.applicationContext, schoolRecyclerView!!,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // view는 item layout이며 position은 현재 recyclerView내의 item의 위치이다.
                        var selectedName =  view.findViewById<TextView>(R.id.school).text.toString()
                        //Toast.makeText(context,selectedName,Toast.LENGTH_LONG).show()
                        schoolName?.text = selectedName
                        // 학교이름창 및 전공선택 스피너, 성적 레이아웃 보이게 하기.
                        if(schoolName?.visibility == View.INVISIBLE)
                            schoolName?.visibility = View.VISIBLE
                        if(majorSelectSpinner?.visibility == View.INVISIBLE)
                            majorSelectSpinner?.visibility= View.VISIBLE
                        if(gradeLayout?.visibility == View.GONE){
                            gradeLayout?.visibility = View.VISIBLE
                        }
                        // Recyclerview item 클릭시 List안보이게.
                        if(schoolRecyclerView?.visibility == View.VISIBLE)
                            schoolRecyclerView?.visibility=View.GONE
                        settingSpinner()
                        closeKeyboard()
                    }
                })
        )
        // 학교검색창(schoolSearchEditText) 의 참조 및 textWatcher 설정.
        schoolSearchEditText = view.findViewById(R.id.signUpSchoolGradeInfoSearchEditText)
        schoolSearchEditText?.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                //Log.d("afterTextChanged","변함")
                //Log.d("afterTextChanged",s.toString())
                // 학교이름창 및 전공선택 스피너, 성적 레이아웃 안보이게 하기.
                if(schoolName?.visibility == View.VISIBLE)
                    schoolName?.visibility = View.INVISIBLE
                if(majorSelectSpinner?.visibility == View.VISIBLE)
                    majorSelectSpinner?.visibility= View.INVISIBLE
                if(gradeLayout?.visibility == View.VISIBLE){
                    gradeLayout?.visibility = View.GONE
                }
                // RecyclerView 보이게 하기.
                if(schoolRecyclerView?.visibility == View.GONE)
                    schoolRecyclerView?.visibility=View.VISIBLE

                recyclerViewAdapter?.searchFilter(s.toString())
                recyclerViewAdapter?.notifyDataSetChanged()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        //
        gradeLayout = view.findViewById(R.id.signUpSchoolGradeInfoGradeLayout)

        // 성적EditText 참조.
        signUpSchoolGradeTotalAverage = view.findViewById(R.id.signUpSchoolGradeInfoTotalAverage)
        signUpSchoolGradeTotalPercentage = view.findViewById(R.id.signUpSchoolGradeInfoTotalPercentage)
        signUpSchoolGradeLastAverage = view.findViewById(R.id.signUpSchoolGradeInfoLastAverage)
        signUpSchoolGradeLastPercentage = view.findViewById(R.id.signUpSchoolGradeInfoLastPercentage)
        // 회원가입 완료버튼
        signUpCompleteButton = view.findViewById(R.id.signUpSchoolGradeInfoComplete)
        signUpCompleteButton?.setOnClickListener(this)


        return view
    }

    // 학교이름/학과/성적 등 현 프레그먼트로 입력받은 모든 데이터 검사.
    fun inspectionSchoolGradeIfo(): Boolean{

        if (schoolName?.text.toString().isEmpty()){
            Toast.makeText(context,R.string.schoolEmptyInspection,Toast.LENGTH_SHORT).show()
            return false
        }
        if(!inspectionMajorSpinner(majorSelectSpinner)){
            return false
        }
        if(!inspectionAverageGrade(signUpSchoolGradeTotalAverage?.
                text?.toString()?.trim()?.replace(" ",""))){
            return false
        }
        if(!inspectionAverageGrade(signUpSchoolGradeLastAverage?.
                text?.toString()?.trim()?.replace(" ",""))){
            return false
        }
        if(!inspectionPercentage(signUpSchoolGradeTotalPercentage?.
                text?.toString()?.trim()?.replace(" ","") )){
            return false
        }
        if(!inspectionPercentage(signUpSchoolGradeLastPercentage?.
                text?.toString()?.trim()?.replace(" ","") )){
            return false
        }
        return true
    }
    // 전공선택 스피너 검사.
    fun inspectionMajorSpinner(spinner: Spinner?):Boolean{

        var major = spinner?.selectedItem as String
        if(major.equals(signUpActivity?.getString(R.string.MajorSpinnerTitle)) || major.isEmpty()){
            Toast.makeText(context,R.string.majorSelectInspection,Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // 4.5이하의 소수점 1-2자리까지인지 검사.(학점검사)
    fun inspectionAverageGrade(average: String?): Boolean{
        var err = false
        if(average?.isNotEmpty()!!){
            val regex = "^[0-4]{1}+(.[0-9]{1,2})?\$"
            val p = Pattern.compile(regex)
            val m = p.matcher(average)
            if (m.matches()) {
                err = true
                return err
            }
        }
        Toast.makeText(context,R.string.gradeInspection,Toast.LENGTH_SHORT).show()
        return err
    }
    //0-100이하의 정수를 입력했는지 검사(백분위검사)
    fun inspectionPercentage(percentage: String?):Boolean{
        try {
            var temp = percentage?.toInt()
            if (temp != null) {
                if((temp > 0 ) && (temp <= 100)){
                    return true
                }
            }
        }catch (e: Exception){
            Toast.makeText(context,R.string.percentageInspection,Toast.LENGTH_SHORT).show()
            return false
        }
        return false
    }

    // 모든 버튼 클릭 리스너
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.signUpSchoolGradeInfoComplete ->{
                if(inspectionSchoolGradeIfo()){
                    createSIgnUpDialog()?.show()
                    // realm 객체 3개 저장(userInfo, address, grade, school)
                    // fireBase에 id pw 저장.(userInfo)
                    // LoginActivity로 돌아감.
                    // signUpActivity?.finish() --> 다이얼로그 이벤트 리스너에서 구현.
                }else{

                }

            }

        }
    }
    // 추가적인 정보의 입력이 필요하다는 다이얼로그.
    fun createSIgnUpDialog(): AlertDialog.Builder? {
        //다이얼로그 생성.
        var dialogBuilder = context?.let { AlertDialog.Builder(it) }
        var title = signUpActivity?.getString(R.string.signUpCompleteDialogTitle)
        var message = signUpActivity?.getString(R.string.signUpCompleteMessage)
        dialogBuilder?.setTitle(title)
        dialogBuilder?.setMessage(message)
        dialogBuilder?.setIcon(R.drawable.scholarship)
        dialogBuilder?.setPositiveButtonIcon(signUpActivity?.getDrawable(R.drawable.ic_check_24dp))
        // 다이얼로그 리스너.
        var dialogListener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->{
                        //Toast.makeText(context,"yes",Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
        dialogBuilder?.setPositiveButton("",dialogListener)

        return dialogBuilder
    }
    //소프트 키보드 강제로 내리기.
    fun closeKeyboard(){
        var view = signUpActivity?.currentFocus
        if(view != null){
            val inputMethodManager = signUpActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    // 학교정보가 정해지면 학과정보를 정해서 스피너 셋팅.
    fun settingSpinner(){
        if(schoolName?.text.toString().isNotEmpty()) {
            var majorData = signUpActivity?.schoolData?.get(schoolName?.text.toString())
            majorData?.sort()
            val spinnerTitle = signUpActivity?.getString(R.string.MajorSpinnerTitle)
            majorData?.add(0, spinnerTitle!!)
            spinnerAdapter = ArrayAdapter(context,android.R.layout.simple_spinner_item, majorData)
            spinnerAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            majorSelectSpinner?.adapter =spinnerAdapter
            var listener = SpinnerListener()
            majorSelectSpinner?.onItemSelectedListener = listener
        }
    }
    // 스피너 리스너 - 추후 학교 데이터 객체생성후 데이터 저장.
    inner class SpinnerListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        }
    }
}
