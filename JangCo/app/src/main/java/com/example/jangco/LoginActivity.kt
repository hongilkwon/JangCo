package com.example.jangco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = "Login"
    private lateinit var auth: FirebaseAuth
    lateinit var loadingDialog: CustomLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        loadingDialog = CustomLoadingDialog(this)

        loginActivitySignUpTextView.setOnClickListener(this)
        loginActivityLoginButton.setOnClickListener(this)

    }

    //로그인 화면의 모든 뷰의 onClick Listener 를 처리한다.
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.loginActivitySignUpTextView -> {
                var intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.loginActivityLoginButton -> {
                // 개발시 유용성을 위해 id pw없이 그냥 로그인.
                var intent = Intent(this, MainActivity::class.java)
                //startActivity(intent)
                var email = loginActivityIDEditText.text.toString().trim()
                var password = loginActivityPWEditText.text.toString().trim()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    loadingDialog.show() // 로그인을위한 인증 시작시 프로그레스 다이얼로그 시작.
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                val user = auth.currentUser
                                startActivity(intent)
                                finish()
                            } else {
                                // If sign in fails, display a message to the user.
                                loadingDialog.dismiss() // 로그인 실패시 프로그레스다이얼로그 종료
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext, R.string.loginFail,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }else{   Toast.makeText(
                    baseContext, R.string.loginEmptyEmailPw,
                    Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    override fun onDestroy() {
        loadingDialog.dismiss()
        super.onDestroy()
    }
}
