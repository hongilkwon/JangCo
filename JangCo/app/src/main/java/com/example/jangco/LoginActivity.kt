package com.example.jangco

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.jangco.CustomLoadingDialog.Companion.LOGIN_LOADING_DIALOG_TYPE
import com.google.firebase.auth.FirebaseAuth
import com.kakao.util.helper.Utility
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = "Login"

    private lateinit var auth: FirebaseAuth
    lateinit var loadingDialog: CustomLoadingDialog


    var permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        permissionCheck()

        Log.d("test", getKeyHash(this))

        auth = FirebaseAuth.getInstance()
        loadingDialog = CustomLoadingDialog(LOGIN_LOADING_DIALOG_TYPE,this)
        loginActivitySignUpTextView.setOnClickListener(this)
        loginActivityLoginButton.setOnClickListener(this)
        // 자동로그인.
        if (savedInstanceState == null) {
            val prefs = getSharedPreferences("person_Info",0)

            val id = prefs.getString("id", "").toString().trim()
            val pw = prefs.getString("pw", "").toString().trim()
            val checked = prefs.getBoolean("checked",false)
            loginActivityIDEditText.setText(id)
            loginActivityPWEditText.setText(pw)
            loginActivityAutoLoginCheckBox.setChecked(checked)

            var intent = Intent(this, MainActivity::class.java)
            login(id, pw, intent)
        }
    }

    private fun permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionList, 0)
        } else {

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(result in grantResults){
            if(result == PackageManager.PERMISSION_DENIED){
                return
            }
        }
    }

    //로그인 화면의 모든 뷰의 onClick Listener 를 처리한다.
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.loginActivitySignUpTextView -> {
                var intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.loginActivityLoginButton -> {
                var intent = Intent(this, MainActivity::class.java)
                var email = loginActivityIDEditText.text.toString().trim()
                var password = loginActivityPWEditText.text.toString().trim()
                login(email, password, intent)
            }
        }
    }

    private fun login(email: String, password: String, intent: Intent) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            loadingDialog.show() // 로그인을위한 인증 시작시 프로그레스 다이얼로그 시작.
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
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
        } else {
            Toast.makeText(
                baseContext, R.string.loginEmptyEmailPw,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        if(loginActivityAutoLoginCheckBox.isChecked) {
            val prefs = getSharedPreferences("person_Info", 0)
            val editor = prefs.edit()
            var id = loginActivityIDEditText.text.toString()
            var pw = loginActivityPWEditText.text.toString()
            editor.putString("id", id)
            editor.putString("pw", pw)
            editor.putBoolean("checked",true)
            editor.apply()
        }
        loadingDialog.dismiss()
        super.onDestroy()
    }

    private fun getKeyHash(context: Context): String? {
        val packageInfo = Utility.getPackageInfo(context, PackageManager.GET_SIGNATURES) ?: return null

        for (signature in packageInfo.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
            } catch (e: NoSuchAlgorithmException) {
                Log.w(TAG, "Unable to get MessageDigest. signature=$signature", e)
            }

        }
        return null
    }
}
