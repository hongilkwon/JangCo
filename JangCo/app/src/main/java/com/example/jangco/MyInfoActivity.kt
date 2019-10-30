package com.example.jangco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_my_info.*

class MyInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)

        settingToolBar()
    }


    private fun settingToolBar(){
        setSupportActionBar(myInfoActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게

        var toolbar = layoutInflater.inflate(R.layout.activity_myinfo_toolbar, null)
        supportActionBar?.customView = toolbar

        var backButton = toolbar.findViewById<ImageView>(R.id.myInfoToolbarBackImageView)
        backButton.setOnClickListener{ view ->
            finish()
        }
    }
}
