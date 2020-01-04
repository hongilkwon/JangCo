package com.example.jangco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_address.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeAddressActivity : AppCompatActivity(), View.OnClickListener {

    private val SEARCH_ADDRESS_ACTIVITY: Int = 10000

    private var userAddress: Address? = null
    private lateinit var dataBaseHelper: DataBaseHelper
    private val firebaseAuth = FirebaseAuth.getInstance()

    lateinit var loadingDialog: CustomLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_address)
        settingToolBar()

        loadingDialog = CustomLoadingDialog(CustomLoadingDialog.DATA_LOADING_DIALOG_TYPE,this)
        loadingDialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        userAddress = intent.getSerializableExtra("userAddress") as Address
        dataBaseHelper = DataBaseHelper(firebaseAuth.currentUser?.email!!)
        changeAddressActivitySearchForAddressButton.setOnClickListener(this)
        changeAddressActivityCancelButton.setOnClickListener(this)
        changeAddressActivityUpdateButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.changeAddressActivitySearchForAddressButton -> {
                val intent = Intent(this, SearchAddressActivity::class.java)
                startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY)
            }
            R.id.changeAddressActivityCancelButton -> {
                finish()
            }
            R.id.changeAddressActivityUpdateButton -> {
                if(inspectionAddress()){
                    loadingDialog.show()
                    CoroutineScope(Dispatchers.IO).launch {
                        userAddress?.cityProvince =
                            changeAddressActivityAddressCityProvince.text.toString()
                        userAddress?.district = changeAddressActivityAddressDistrict.text.toString()
                        dataBaseHelper.upDateUserAddress(userAddress!!)

                        intent.putExtra("userAddress", userAddress)
                        setResult(RESULT_OK, intent)
                        loadingDialog.dismiss()
                        finish()
                    }
                }
            }
        }
    }

    private fun inspectionAddress(): Boolean {
        if (!changeAddressActivityAddressCityProvince.text.toString().isNullOrEmpty() &&
            !changeAddressActivityAddressDistrict.text.toString().isNullOrEmpty() &&
            !changeAddressActivityAddressDetails.text.toString().isNullOrEmpty()
        ) {
            return true
        }
        Toast.makeText(this, "주소를 잘못 입력하였습니다.", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SEARCH_ADDRESS_ACTIVITY ->
                if (resultCode == RESULT_OK) {
                    val data = data?.extras!!.getString("data")
                    if (data != null) {
                        var address = data
                        var addressList: List<String>? = address.split(" ")
                        var addressSize = addressList?.size

                        changeAddressActivityAddressLayout.visibility = View.VISIBLE
                        changeAddressActivityAddressDetails.visibility = View.VISIBLE

                        for( i in 0..addressSize!!-1!!){
                            if(i == 0)
                                changeAddressActivityAddressDetails.setText(addressList?.get(0))
                            else if(i == 1)
                                changeAddressActivityAddressCityProvince.setText(addressList?.get(1))
                            else if(i == 2)
                                changeAddressActivityAddressDistrict.setText(addressList?.get(2))
                            else
                                changeAddressActivityAddressDetails.append(addressList?.get(i)+" ")
                        }
                    }else{
                        Toast.makeText(this, R.string.nullAddressAlert, Toast.LENGTH_LONG).show()
                    }
                }
        }

    }

    private fun settingToolBar(){
        setSupportActionBar(changeAddressActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게

        var toolbar = layoutInflater.inflate(R.layout.activity_change_toolbar, null)
        supportActionBar?.customView = toolbar

        var title = toolbar.findViewById<TextView>(R.id.changeToolbarTitleTextView)
        title.text = getString(R.string.moreInfoFragmentMyInfoAddressChange)


        var closeButton = toolbar.findViewById<ImageView>(R.id.changeToolbarBackImageView)
        closeButton.setOnClickListener { view ->
            finish()
        }
    }
}
