package com.example.jangco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_pw.*
import java.util.regex.Pattern

class ChangePwActivity : AppCompatActivity(), View.OnClickListener {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pw)
        settingToolBar()
        changePwActivityCancelButton.setOnClickListener(this)
        changePwActivityUpdateButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.changePwActivityCancelButton ->
                finish()
            R.id.changePwActivityUpdateButton -> {
                // 기존 비밀번호가 맞는지 확인.
                var email = auth.currentUser?.email
                var previousPW = changePwActivityPreviousPwEditText.text.toString().trim().replace(" ", "")
                if(inspectionPW(changePwActivityNewPwEditText, changePwActivityNewPwConfirmEditText)){
                   // 유저 재인증, 유저 재인증이 성공하면 비밀번호를 변경함.
                    reauthenticate(email!!,previousPW)
                }
            }
        }
    }

    private fun settingToolBar(){
        setSupportActionBar(changePwActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게

        var toolbar = layoutInflater.inflate(R.layout.activity_change_toolbar, null)
        supportActionBar?.customView = toolbar


        var title = toolbar.findViewById<TextView>(R.id.changeToolbarTitleTextView)
        title.text = getString(R.string.moreInfoFragmentMyInfoPasswordChange)


        var closeButton = toolbar.findViewById<ImageView>(R.id.changeToolbarBackImageView)
        closeButton.setOnClickListener { view ->
            finish()
        }
    }
    // 기존 비밀번호를 통한 재인증.
    private fun reauthenticate(email: String,password: String){
        val credential = EmailAuthProvider
            .getCredential(email,password)
        auth?.currentUser?.reauthenticate(credential)
            ?.addOnCompleteListener(this){
                if(it.isSuccessful){
                    changePw()
                    finish()
                }
            }
    }

    //비밀번호 변경.
    private fun changePw() {
        var newPw = changePwActivityNewPwEditText.text.toString()
        auth.currentUser?.updatePassword(newPw)?.addOnCompleteListener(this) {
            if (it.isSuccessful) {
                //패스워드 변경이 성공했을 때 발생하는 이벤트
                Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inspectionPW(newPwEditText: EditText, newPwConfirmEditText: EditText): Boolean{
        // 새비밀번호와 확인비번을 받아서 비어있는지 확인.
        if(isEmptyEditText(newPwEditText) || isEmptyEditText(newPwConfirmEditText)) {
            Toast.makeText(this, getString(R.string.pwEmptyInspection), Toast.LENGTH_SHORT).show()
            return false
        }
        // 비어 있지않다면 2개의 String 추출.
        var newPw = newPwEditText.text.toString().trim().replace(" ","")
        var newPwConfirm = newPwConfirmEditText.text.toString().trim().replace(" ","")
        if(!isPwSame(newPw, newPwConfirm)){
            Toast.makeText(this, getString(R.string.pwConfirmInspection), Toast.LENGTH_SHORT).show()
            return false
        }
        // 비밀번호 형식에 맞는지 확인.
        if(!validationPW(newPw)){
            Toast.makeText(this, getString(R.string.pwConditionInspection), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    //비밀번호 일치 확인.
    private fun isPwSame(newPw: String, newPwConfirm: String): Boolean {
        if(newPw.equals(newPwConfirm)){
            return true
        }
        return false
    }
    // 빈 EditText 인지 확인.
    private fun isEmptyEditText(editText: EditText): Boolean {
        val str = editText.text.toString().trim().replace(" ", "")
        if (str.isEmpty()) {
            return true
        }
        return false
    }
    // 비밀번호 유효성 확인.
    private fun validationPW(pw: String): Boolean {
        val p = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")
        val m = p.matcher(pw)
        if (m.matches()) {
            return true
        }
        return false
    }
}
