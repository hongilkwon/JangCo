package com.example.jangco

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide


class GuideViewPagerAdapter(val context: Context, val guideImageList: ArrayList<Drawable>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.

        val inflater = LayoutInflater.from(context)
        var view = inflater.inflate(R.layout.activity_guide_item, container, false)
        val guideImageView = view.findViewById<ImageView>(R.id.guideImageView)
        guideImageView.setImageDrawable(guideImageList[position])
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
       return guideImageList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView( `object` as View)
    }
}