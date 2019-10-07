package com.example.jangco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginActivitySignUpTextView.setOnClickListener(this)
        loginActivityLoginButton.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        //로그인 화면의 모든 뷰의 onClick Listener 를 처리한다.
        when(v?.id){
            R.id.loginActivitySignUpTextView -> {
                var intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.loginActivityLoginButton -> {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
        }
    }
}
