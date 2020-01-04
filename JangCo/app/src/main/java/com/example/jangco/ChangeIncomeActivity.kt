package com.example.jangco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_income.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ChangeIncomeActivity : AppCompatActivity(), View.OnClickListener, TextWatcher {

    private var userIncome: Income? = null
    lateinit var loadingDialog: CustomLoadingDialog
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var dataBaseHelper: DataBaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_income)
        settingToolBar()
        loadingDialog = CustomLoadingDialog(CustomLoadingDialog.DATA_LOADING_DIALOG_TYPE,this)
        loadingDialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dataBaseHelper = DataBaseHelper(firebaseAuth.currentUser?.email!!)

        changeIncomeActivityCancelButton.setOnClickListener(this)
        changeIncomeActivityUpdateButton.setOnClickListener(this)

        changeIncomeActivityTierRadioGroup.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId){
                R.id.changeIncomeActivityBasicTierCheckBox -> {
                    userIncome = Income(1,30)
                    changeIncomeActivityFamilyMonthIncomeEditText.setText("")
                    changeIncomeActivityFamilyNumberEditText.setText("")
                    changeIncomeActivityETCLayout.visibility = View.GONE
                }
                R.id.changeIncomeActivitySecondTierCheckBox -> {
                    userIncome = Income(2, 50)
                    changeIncomeActivityFamilyMonthIncomeEditText.setText("")
                    changeIncomeActivityFamilyNumberEditText.setText("")
                    changeIncomeActivityETCLayout.visibility = View.GONE
                }
                R.id.changeIncomeActivityETCCheckBox ->
                    changeIncomeActivityETCLayout.visibility = View.VISIBLE
            }
        }

        changeIncomeActivityFamilyMonthIncomeEditText.addTextChangedListener(this)
        changeIncomeActivityFamilyNumberEditText.addTextChangedListener(this)


    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.changeIncomeActivityCancelButton -> {
                finish()
            }
            R.id.changeIncomeActivityUpdateButton -> {

                loadingDialog.show()
                CoroutineScope(Dispatchers.IO).launch {
                    dataBaseHelper.upDateUserIncome(userIncome!!)
                    intent.putExtra("userIncome", userIncome)
                    setResult(RESULT_OK, intent)
                    loadingDialog.dismiss()
                    finish()

                }
            }
        }
    }

    private fun inspectionFamNumAndFamMonthIncome(): Boolean {
        var famNum = changeIncomeActivityFamilyNumberEditText.text.toString()
        var famMonthIncome = changeIncomeActivityFamilyMonthIncomeEditText.text.toString()

        if (famNum.isNullOrEmpty() || famMonthIncome.isNullOrEmpty()) {
            changeIncomeActivityFamilyMedianIncomePercentageTextView.setText(getString(R.string.medianIncomePercentage))
            return false
        }

        try {
            famNum.toInt()
            famMonthIncome.toInt()
        } catch (e: Exception) {
            Toast.makeText(this, "가족수 또는 월수입을 제대로 입력하세요", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun settingToolBar(){
        setSupportActionBar(changeIncomeActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게

        var toolbar = layoutInflater.inflate(R.layout.activity_change_toolbar, null)
        supportActionBar?.customView = toolbar

        var title = toolbar.findViewById<TextView>(R.id.changeToolbarTitleTextView)
        title.text = getString(R.string.moreInfoFragmentMyInfoIncomeChange)

        var closeButton = toolbar.findViewById<ImageView>(R.id.changeToolbarBackImageView)
        closeButton.setOnClickListener { view ->
            finish()
        }
    }

    private fun calMedianIncomePercentageAndIncomePercentage(famNum: String, famMonthIncome: String): Int {

        var medianIncomeStandardArr = arrayOf(176, 299, 387, 475, 563, 651, 739)

        var num = famNum.toInt()
        var income = famMonthIncome.toInt()
        var m = 0
        if(num > 7)
            m = medianIncomeStandardArr[6]+((num-7)*83)
        else
            m = medianIncomeStandardArr[num-1]

        var mp= (100 * income) / m

        var ip = 10
        var incomePercentageArr = arrayOf(30, 50, 70, 90, 100, 130, 150, 200, 300)

        for(i in incomePercentageArr.indices) {
            if(mp <= incomePercentageArr[i]){
                ip = i + 1
                break
            }
        }
        userIncome= Income(ip.toLong() ,mp.toLong())
        return mp
    }
    override fun afterTextChanged(s: Editable?) {
        var famNum = changeIncomeActivityFamilyNumberEditText.text.toString()
        var famMonthIncome = changeIncomeActivityFamilyMonthIncomeEditText.text.toString()

        if (inspectionFamNumAndFamMonthIncome()){
            var mp = calMedianIncomePercentageAndIncomePercentage(famNum,famMonthIncome)
            changeIncomeActivityFamilyMedianIncomePercentageTextView.setText(String.format("%s %s%s", getString(R.string.medianIncomePercentage), mp.toString(),"%"))
        }
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}
