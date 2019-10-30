package com.example.jangco

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_guide.*

class GuideActivity : AppCompatActivity() {


    var guideList = ArrayList<Drawable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        // 상태바 색상 변경.
        window.statusBarColor = getColor(R.color.appMainColor)

        // 임시 guide 이미지들입니다.
        guideList.add(getDrawable(R.drawable.ic_temp_guide_1))
        guideList.add(getDrawable(R.drawable.ic_temp_guide_2))
        guideList.add(getDrawable(R.drawable.ic_temp_guide_3))

        guideActivityViewPager.adapter = GuideViewPagerAdapter(this, guideList)


        guideActivityTabLayout.setupWithViewPager(guideActivityViewPager)

        guideActivityCloseButton.setOnClickListener { view ->
            finish()
        }
    }

}
