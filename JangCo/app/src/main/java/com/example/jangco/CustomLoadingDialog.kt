package com.example.jangco

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.ImageView

class CustomLoadingDialog(context: Context?) : ProgressDialog(context) {
    private var imgLogo: ImageView? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
    }
    // savedInstanceState: Bundle->? 붙혀주면 잘작동되는데 안붙혀주면 널값체크 에러가 뜬다.
    // 왜그런지 모르겠다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog)

        imgLogo = findViewById(R.id.img_android) as ImageView
        val anim = AnimationUtils.loadAnimation(context, R.anim.loading)
        imgLogo!!.animation = anim
    }

}