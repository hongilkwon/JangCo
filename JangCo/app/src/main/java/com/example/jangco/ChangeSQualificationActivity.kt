package com.example.jangco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_grade.*
import kotlinx.android.synthetic.main.activity_change_squalification.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeSQualificationActivity : AppCompatActivity(), View.OnClickListener {

    private var userSQualification: SQualification? = null
    private lateinit var dataBaseHelper: DataBaseHelper
    private val firebaseAuth = FirebaseAuth.getInstance()

    lateinit var loadingDialog: CustomLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_squalification)
        settingToolBar()

        loadingDialog = CustomLoadingDialog(CustomLoadingDialog.DATA_LOADING_DIALOG_TYPE, this)
        loadingDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        userSQualification = intent.getSerializableExtra("userSQualification") as SQualification
        changeSQualificationActivityOfficialCheckBox.isChecked = userSQualification?.official!!
        changeSQualificationActivityNationalMeritCheckBox.isChecked =
            userSQualification?.nationalMerit!!
        changeSQualificationActivityIndustrialAccidentCheckBox.isChecked =
            userSQualification?.industrialAccident!!
        changeSQualificationActivityActiveSoldierCheckBox.isChecked =
            userSQualification?.activeSoldier!!
        changeSQualificationActivityLongTermSoldierCheckBox.isChecked =
            userSQualification?.longTermSoldier!!
        changeSQualificationActivityPrivateSchoolCheckBox.isChecked =
            userSQualification?.privateSchool!!
        changeSQualificationActivityEmploymentInsuranceCheckBox.isChecked =
            userSQualification?.employmentInsurance!!
        changeSQualificationActivityStudentHeadCheckBox.isChecked =
            userSQualification?.studentHead!!
        changeSQualificationActivitySingleParentFamilyCheckBox.isChecked =
            userSQualification?.singleParentFamily!!
        changeSQualificationActivityDisabledCheckBox.isChecked = userSQualification?.disabled!!
        changeSQualificationActivityMulticulturalFamilyCheckBox.isChecked =
            userSQualification?.multiculturalFamily!!
        changeSQualificationActivityAgricultureForestryFisheriesCheckBox.isChecked =
            userSQualification?.agricultureForestryFisheries!!
        changeSQualificationActivityNorthKoreaDefectorCheckBox.isChecked =
            userSQualification?.northKoreaDefector!!
        changeSQualificationActivityHighwayAccidentCheckBox.isChecked =
            userSQualification?.highwayAccident!!
        changeSQualificationActivitySmallBusinessCheckBox.isChecked =
            userSQualification?.smallBusiness!!
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.changeSQualificationActivityCancelButton -> {
                finish()
            }
            R.id.changeSQualificationActivityUpdateButton -> {
                loadingDialog.show()
                CoroutineScope(Dispatchers.IO).launch {
                    userSQualification?.official =
                        changeSQualificationActivityOfficialCheckBox.isChecked
                    userSQualification?.nationalMerit =
                        changeSQualificationActivityNationalMeritCheckBox.isChecked
                    userSQualification?.industrialAccident =
                        changeSQualificationActivityIndustrialAccidentCheckBox.isChecked
                    userSQualification?.activeSoldier =
                        changeSQualificationActivityActiveSoldierCheckBox.isChecked
                    userSQualification?.longTermSoldier =
                        changeSQualificationActivityLongTermSoldierCheckBox.isChecked
                    userSQualification?.privateSchool =
                        changeSQualificationActivityPrivateSchoolCheckBox.isChecked
                    userSQualification?.employmentInsurance =
                        changeSQualificationActivityEmploymentInsuranceCheckBox.isChecked
                    userSQualification?.studentHead =
                        changeSQualificationActivityStudentHeadCheckBox.isChecked
                    userSQualification?.singleParentFamily =
                        changeSQualificationActivitySingleParentFamilyCheckBox.isChecked
                    userSQualification?.disabled =
                        changeSQualificationActivityDisabledCheckBox.isChecked
                    userSQualification?.multiculturalFamily =
                        changeSQualificationActivityMulticulturalFamilyCheckBox.isChecked
                    userSQualification?.agricultureForestryFisheries =
                        changeSQualificationActivityAgricultureForestryFisheriesCheckBox.isChecked
                    userSQualification?.northKoreaDefector =
                        changeSQualificationActivityNorthKoreaDefectorCheckBox.isChecked
                    userSQualification?.highwayAccident =
                        changeSQualificationActivityHighwayAccidentCheckBox.isChecked
                    userSQualification?.smallBusiness =
                        changeSQualificationActivitySmallBusinessCheckBox.isChecked

                    dataBaseHelper.upDateUsersQualification(userSQualification!!)

                    intent.putExtra("userSQualification", userSQualification)
                    setResult(RESULT_OK, intent)
                    loadingDialog.dismiss()
                    finish()
                }

            }
        }
    }

    private fun settingToolBar() {
        setSupportActionBar(changeSQualificationActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게

        var toolbar = layoutInflater.inflate(R.layout.activity_change_toolbar, null)
        supportActionBar?.customView = toolbar

        var title = toolbar.findViewById<TextView>(R.id.changeToolbarTitleTextView)
        title.text = getString(R.string.moreInfoFragmentMyInfoSqualificationChange)


        var closeButton = toolbar.findViewById<ImageView>(R.id.changeToolbarBackImageView)
        closeButton.setOnClickListener { view ->
            finish()
        }
    }

}
