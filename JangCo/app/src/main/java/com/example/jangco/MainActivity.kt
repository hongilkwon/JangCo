package com.example.jangco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class MainActivity : AppCompatActivity() {


    val HOME_FRAGMENT: Int = 1
    val SCHOLARSHIP_FRAGMENT: Int = 2
    val MY_SCHOLARSHIP_FRAGMENT: Int = 3
    val MY_INFO_FRAGMENT: Int = 4

    private var fragmentManager = supportFragmentManager
    var homeFragment = HomeFragment()
    var scholarshipFragment = ScholarshipFragment()
    var myScholarshipFragment = MyScholarshipFragment()
    var myInfoFragment = MyInfoFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 첫 프레그먼트 설정.
        settingFragment(HOME_FRAGMENT)
        settingToolbar(HOME_FRAGMENT)
        mainActivityBottomNavigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.mHome -> {
                        settingFragment(HOME_FRAGMENT)
                        settingToolbar(HOME_FRAGMENT)
                    }
                    R.id.mScholarship -> {
                        settingFragment(SCHOLARSHIP_FRAGMENT)
                        settingToolbar(SCHOLARSHIP_FRAGMENT)
                    }
                    R.id.mMyScholarship -> {
                        settingFragment(MY_SCHOLARSHIP_FRAGMENT)
                        settingToolbar(MY_SCHOLARSHIP_FRAGMENT)
                    }
                    R.id.mMyInfo -> {
                        settingFragment(MY_INFO_FRAGMENT)
                        settingToolbar(MY_INFO_FRAGMENT)
                    }
                }
                return true
            }
        })
    }


    fun settingFragment(fragmentCode: Int) {
        var tran = fragmentManager.beginTransaction()
        when (fragmentCode) {
            HOME_FRAGMENT -> {
                tran.replace(R.id.mainActivityFrameLayout, homeFragment)
            }
            SCHOLARSHIP_FRAGMENT -> {
                tran.replace(R.id.mainActivityFrameLayout, scholarshipFragment)
            }
            MY_SCHOLARSHIP_FRAGMENT -> {
                tran.replace(R.id.mainActivityFrameLayout, myScholarshipFragment)
            }
            MY_INFO_FRAGMENT -> {

                tran.replace(R.id.mainActivityFrameLayout, myInfoFragment)
            }
        }
        tran.commit()
    }

    fun settingToolbar(fragmentCode: Int) {
        setSupportActionBar(mainActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게
        mainActivityToolbar.removeAllViews()
        when (fragmentCode) {
            HOME_FRAGMENT -> {
               var toolbar = layoutInflater.inflate(R.layout.fragment_home_toolbar, null)
                mainActivityToolbar.addView(toolbar)
            }
            SCHOLARSHIP_FRAGMENT -> {
               var toolbar = layoutInflater.inflate(R.layout.fragment_scholarship_toolbar, null)
                mainActivityToolbar.addView(toolbar)
            }
            MY_SCHOLARSHIP_FRAGMENT -> {
               var toolbar = layoutInflater.inflate(R.layout.fragment_my_scholarship_toolbar, null)
                mainActivityToolbar.addView(toolbar)
            }
            MY_INFO_FRAGMENT -> {
               var toolbar = layoutInflater.inflate(R.layout.fragment_my_info_toolbar, null)
                mainActivityToolbar.addView(toolbar)
            }
        }
    }
}
