package com.example.jangco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_address.*
import kotlinx.android.synthetic.main.activity_change_grade.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.regex.Pattern

class ChangeGradeActivity : AppCompatActivity(), View.OnClickListener {


    private var userGrade: Grade? = null
    private lateinit var dataBaseHelper: DataBaseHelper
    private val firebaseAuth = FirebaseAuth.getInstance()

    lateinit var loadingDialog: CustomLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_grade)
        settingToolBar()
        loadingDialog = CustomLoadingDialog(CustomLoadingDialog.DATA_LOADING_DIALOG_TYPE,this)
        loadingDialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        userGrade = intent.getSerializableExtra("userGrade") as Grade
        changeGradeActivityTotalAverage.setText( userGrade!!.totalAverageGrade.toString())
        changeGradeActivityTotalPercentage.setText(userGrade!!.totalPercentage.toString())
        changeGradeActivityLastAverage.setText(userGrade!!.lastAverageGrade.toString())
        changeGradeActivityLastPercentage.setText(userGrade!!.lastPercentage.toString())

        dataBaseHelper = DataBaseHelper(firebaseAuth.currentUser?.email!!)

        changeGradeActivityCancelButton.setOnClickListener(this)
        changeGradeActivityUpdateButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.changeGradeActivityCancelButton -> {
                finish()
            }
            R.id.changeGradeActivityUpdateButton -> {
                if(inspectionGrade()){
                    loadingDialog.show()
                    CoroutineScope(Dispatchers.IO).launch {
                        userGrade?.totalAverageGrade =
                            changeGradeActivityTotalAverage.text.toString().toDouble()
                        userGrade?.totalPercentage =
                            changeGradeActivityTotalPercentage.text.toString().toLong()
                        userGrade?.lastAverageGrade =
                            changeGradeActivityLastAverage.text.toString().toDouble()
                        userGrade?.lastPercentage =
                            changeGradeActivityLastPercentage.text.toString().toLong()

                        dataBaseHelper.upDateUserGrade(userGrade!!)

                        intent.putExtra("userGrade", userGrade)
                        setResult(RESULT_OK, intent)
                        loadingDialog.dismiss()
                        finish()
                    }
                }
            }
        }
    }

    fun inspectionGrade(): Boolean{
        if(!inspectionAverageGrade(changeGradeActivityTotalAverage?.
                text?.toString()?.trim()?.replace(" ",""))){
            return false
        }
        if(!inspectionAverageGrade(changeGradeActivityLastAverage?.
                text?.toString()?.trim()?.replace(" ",""))){
            return false
        }
        if(!inspectionPercentage(changeGradeActivityTotalPercentage?.
                text?.toString()?.trim()?.replace(" ","") )){
            return false
        }
        if(!inspectionPercentage(changeGradeActivityLastPercentage?.
                text?.toString()?.trim()?.replace(" ","") )){
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
        Toast.makeText(this,R.string.gradeInspection, Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this,R.string.percentageInspection, Toast.LENGTH_SHORT).show()
            return false
        }
        return false
    }

    private fun settingToolBar(){
        setSupportActionBar(changeGradeActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게

        var toolbar = layoutInflater.inflate(R.layout.activity_change_toolbar, null)
        supportActionBar?.customView = toolbar

        var title = toolbar.findViewById<TextView>(R.id.changeToolbarTitleTextView)
        title.text = getString(R.string.moreInfoFragmentMyInfoGradeChange)

        var closeButton = toolbar.findViewById<ImageView>(R.id.changeToolbarBackImageView)
        closeButton.setOnClickListener { view ->
            finish()
        }
    }
}
