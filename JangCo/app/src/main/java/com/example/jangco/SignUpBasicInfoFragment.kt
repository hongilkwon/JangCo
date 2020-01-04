package com.example.jangco


import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.regex.Pattern


class SignUpBasicInfoFragment : Fragment(), View.OnClickListener, TextView.OnEditorActionListener {

    var signUpActivity: SignUpActivity? = null

    var userEmail: EditText? = null
    var userPw: EditText? = null
    var userConfirmPw: EditText? = null

    var searchAddressButton: Button? =null

    var cityProvince: EditText? = null
    var district: EditText? = null
    var detailAddress: EditText? = null
    var nextStepButton: Button? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_sign_up_basic_info, container, false)

        userEmail =  view.findViewById(R.id.signUpBasicInfoFragmentEmailEditText)
        userEmail?.setOnEditorActionListener(this)

        userPw = view.findViewById(R.id.signUpBasicInfoFragmentPWEditText)
        userPw?.setOnEditorActionListener(this)
        var textWatcher1 = PwConfirmTextWatcher(userConfirmPw)
        userPw?.addTextChangedListener(textWatcher1)

        userConfirmPw = view.findViewById(R.id.signUpBasicInfoFragmentPWConfirmEditText)
        userConfirmPw?.setOnEditorActionListener(this)
        var textWatcher2 = PwConfirmTextWatcher(userPw)
        userConfirmPw?.addTextChangedListener(textWatcher2)

        searchAddressButton = view.findViewById(R.id.signUpBasicInfoFragmentSearchAddressButton)
        searchAddressButton?.setOnClickListener(this)

        signUpActivity = activity as SignUpActivity

        cityProvince = view.findViewById(R.id.signUpBasicInfoFragmentAddressCityProvince)
        district = view.findViewById(R.id.signUpBasicInfoFragmentAddressDistrict)
        detailAddress = view.findViewById(R.id.signUpBasicInfoFragmentAddressDetails)
        nextStepButton = view.findViewById(R.id.signUpBasicInfoFragmentNextButton)
        nextStepButton?.setOnClickListener(this)

        return view
    }
    // 정상적인 이메일인지 검사함.
    fun isValidEmail(email: String): Boolean {
        var err = false
        val regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"
        val p = Pattern.compile(regex)
        val m = p.matcher(email)
        if (m.matches()) {
            err = true
        }
        return err
    }
   // 비어있는 EditText인지 확인함.
    fun isEditEmpty(edit: EditText?): Boolean{
        if(edit?.text.toString().trim().replace(" ","").isEmpty()){
           // Log.d("isEditEmpty", "${edit?.text.toString().trim().length}")
            return true
        }
        return false
    }
    // 다음 프레그먼트로 진행하기전에 모든 데이터 검사. 
    fun inspectionBasicInfo(): Boolean{
        //이메일이 아닌 것을 입력했을경우
        if( !isValidEmail(userEmail?.text.toString().trim()) ){
            Toast.makeText(signUpActivity, R.string.idFormatInspection, Toast.LENGTH_SHORT).show()
            return false
        }
        // 아이디 입력안한경우.
        if( isEditEmpty(userEmail) ){
            Toast.makeText(signUpActivity, R.string.idEmptyInspection, Toast.LENGTH_SHORT).show()
            return false
        }
        // 비밀번호를 입력안한경우
        if( isEditEmpty(userPw) || isEditEmpty(userConfirmPw) ){
            Toast.makeText(signUpActivity, R.string.pwEmptyInspection, Toast.LENGTH_SHORT).show()
            return false
        }
        // 비밀번호 확인이 안된경우
        if( !userPw?.text.toString().trim().replace(" ","").equals(userConfirmPw?.text.toString().trim().replace(" ","")) ){
            Toast.makeText(signUpActivity, R.string.pwConfirmInspection, Toast.LENGTH_SHORT).show()
            return false
        }
        //주소를 입력안한경우
        if( isEditEmpty(cityProvince) || isEditEmpty(district) ){
            Toast.makeText(signUpActivity,  R.string.addressEmptyInspection, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    // OnClick 리스너
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.signUpBasicInfoFragmentSearchAddressButton -> {
                signUpActivity?.startSearchAddressActivity()
            }
            R.id.signUpBasicInfoFragmentNextButton -> {
                if(inspectionBasicInfo()){
                    // 유저정보 객체생성
                    signUpActivity?.newAccount = NewAccount(userEmail?.text.toString().trim().replace(" ","")
                    , userPw?.text.toString().trim().replace(" ",""))
                    // 주소정보 객체 생성
                    signUpActivity?.address = Address(cityProvince?.text.toString(), district?.text.toString())
                    signUpActivity?.settingFragment(signUpActivity!!.SIGNUP_SCHOOL_GRADE_INFO)
                }
            }
        }
    }
    // Enterkey 리스너.
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        when(v?.id){
            R.id.signUpBasicInfoFragmentEmailEditText -> {
                var email = userEmail?.text.toString().trim().replace(" ","")
                if(!isValidEmail(email)){
                    Toast.makeText(signUpActivity, R.string.idFormatInspection,Toast.LENGTH_SHORT).show()
                }
            }
            R.id.signUpBasicInfoFragmentPWEditText -> {
                var pw = userPw?.text.toString().trim().replace(" ","")
                if(pw.length < 9){
                    Toast.makeText(signUpActivity, R.string.pwConditionInspection,Toast.LENGTH_SHORT).show()
                }
            }
            R.id.signUpBasicInfoFragmentPWConfirmEditText -> {
                //.....
            }
        }
        return false
    }

    // 텍스트 왓쳐 내부 클래스
    inner class PwConfirmTextWatcher(var editText: EditText?): android.text.TextWatcher{

        override fun afterTextChanged(s: Editable?) {
            if(s.toString().trim().replace(" ","").equals(editText?.text.toString().trim().replace(" ",""))){
                userConfirmPw?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_ok_36dp,0,0,0)
            }else{
                userConfirmPw?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_no_36dp,0,0,0)
            }
            if(editText?.text.toString().trim().replace(" ","").isEmpty()){
                userConfirmPw?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_no_36dp,0,0,0)
            }
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}
