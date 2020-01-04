package com.example.jangco

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window

class CustomLoadingDialog(var type: Int, context: Context?) : ProgressDialog(context) {
//    private var imgLogo: ImageView? = null


    companion object {
        val LOGIN_LOADING_DIALOG_TYPE = 1
        val DATA_LOADING_DIALOG_TYPE = 2
    }
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
    }
    // savedInstanceState: Bundle->? 붙혀주면 잘작동되는데 안붙혀주면 널값체크 에러가 뜬다.
    // 왜그런지 모르겠다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when(type){
            LOGIN_LOADING_DIALOG_TYPE -> setContentView(R.layout.login_loading_dialog)
            DATA_LOADING_DIALOG_TYPE -> setContentView(R.layout.data_loading_dialog)
        }
    }

}