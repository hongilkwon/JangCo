package com.example.jangco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_change_squalification.*

class ChangeSQualificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_squalification)
        settingToolBar()
    }

    private fun settingToolBar(){
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
