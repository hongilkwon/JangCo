package com.example.jangco


import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.jangco.CustomLoadingDialog.Companion.DATA_LOADING_DIALOG_TYPE
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    // 서버연동을 위한 Firebase 관련 객체.
    private lateinit var auth: FirebaseAuth
    lateinit var currentUser: FirebaseUser
    private lateinit var dataBaseHelper: DataBaseHelper

    // 프레그먼트 전환 고유 코드 및 프레그먼트들 할당.
    val HOME_FRAGMENT: Int = 1
    val SCHOLARSHIP_FRAGMENT: Int = 2
    val MY_SCHOLARSHIP_FRAGMENT: Int = 3
    val MORE_INFO_FRAGMENT: Int = 4

    private var fragmentManager = supportFragmentManager
    var homeFragment = HomeFragment()
    var scholarshipFragment = ScholarshipFragment()
    var myScholarshipFragment = MyScholarshipFragment()
    var moreInfoFragment = MoreInfoFragment()

    // 유저의 데이터 객체들.
    var userAllInfo: HashMap<String, Any>? = null

    var userProfile: User? = null
    var userAddress: Address? = null
    var userSchool: School? = null
    var userGrade: Grade? = null
    var userIncome: Income? =  null
    var userSQualification: SQualification? = null
    // 데이터 로딩시 다이얼로그
    lateinit var loadingDialog: CustomLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //데이터 로딩 다이얼로그 초기화
        loadingDialog = CustomLoadingDialog(DATA_LOADING_DIALOG_TYPE,this)
        loadingDialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        //인증 객체 받아오기.
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!
        // 데이터베이스 접속.
        dataBaseHelper = DataBaseHelper(currentUser.email!!)
        // background에서 유저 정보 가져오기
        loadUserData()


        // 첫 프레그먼트 설정.
        settingFragment(HOME_FRAGMENT)
        settingToolbar("홈")
        mainActivityBottomNavigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.mHome -> {
                        settingFragment(HOME_FRAGMENT)
                        settingToolbar("홈")
                    }
                    R.id.mScholarship -> {
                        settingFragment(SCHOLARSHIP_FRAGMENT)
                        settingToolbar("장학금")
                    }
                    R.id.mMyScholarship -> {
                        settingFragment(MY_SCHOLARSHIP_FRAGMENT)
                        settingToolbar("내 장학금 관리")
                    }
                    R.id.mMyInfo -> {
                        settingFragment(MORE_INFO_FRAGMENT)
                        settingToolbar("더 보기")
                    }
                }
                return true
            }
        })
    }

    // DB헬퍼를 통해 최초 로그인시 onCreate에서 모든 정보를 서버에서 로딩시킨다.
    private fun loadUserData() {
        // 코루틴 시작전에 프로그레스 다이얼로그 생성.
        loadingDialog.show()
        CoroutineScope(IO).launch {
            userAllInfo = dataBaseHelper.getUserAllInfo()
            updateData()
            // 코루틴 종료전에 프로그레스 다이얼로그 삭제.
            loadingDialog.dismiss()
        }
    }


    fun updateData() {
        for (data in userAllInfo!!) {
            var obj = data.value
            if (obj is User) {
                userProfile = obj
            } else if (obj is Address) {
                userAddress = obj
            } else if (obj is School) {
                userSchool = obj
            } else if (obj is Grade) {
                userGrade = obj
            }
            if (obj is Income) {
                userIncome = obj
            }
            if (obj is SQualification) {
                userSQualification = obj
            }
        }
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

    fun settingToolbar(title: String) {
        setSupportActionBar(mainActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게

        mainActivityToolbarTitle.text = title

    }


}
