package com.example.jangco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    lateinit var currentUser: FirebaseUser

    val HOME_FRAGMENT: Int = 1
    val SCHOLARSHIP_FRAGMENT: Int = 2
    val MY_SCHOLARSHIP_FRAGMENT: Int = 3
    val MORE_INFO_FRAGMENT: Int = 4

    private var fragmentManager = supportFragmentManager
    var homeFragment = HomeFragment()
    var scholarshipFragment = ScholarshipFragment()
    var myScholarshipFragment = MyScholarshipFragment()
    var moreInfoFragment = MoreInfoFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //인증 객체 받아오기.
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

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
                        settingFragment(MORE_INFO_FRAGMENT)
                        settingToolbar(MORE_INFO_FRAGMENT)
                    }
                }
                return true
            }
        })
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
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
            MORE_INFO_FRAGMENT -> {

                tran.replace(R.id.mainActivityFrameLayout, moreInfoFragment)
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
            MORE_INFO_FRAGMENT -> {
               var toolbar = layoutInflater.inflate(R.layout.fragment_more_info_toolbar, null)
                mainActivityToolbar.addView(toolbar)
            }
        }
    }
}
