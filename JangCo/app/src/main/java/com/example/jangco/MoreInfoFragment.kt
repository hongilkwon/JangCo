package com.example.jangco


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast


class MoreInfoFragment : Fragment(), View.OnClickListener{

    private val MYINFO_ACTIVITY = 1
    private var mainActivity: MainActivity? = null

    private var myInfoIcon: ImageView? = null

    private  var basicComment: TextView? = null
    private var noticeTextView: TextView? = null
    private var eventTextView: TextView? = null
    private var guideTextView: TextView? = null
    private  var personalQuestionTextView: TextView? = null
    private  var settingTextView: TextView? = null
    private  var shareAppTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_more_info, container, false)
        mainActivity = activity as MainActivity

        //코멘트
        basicComment = view.findViewById(R.id.moreInfoFragmentBasicComment) as TextView
        basicComment!!.text =  mainActivity!!.userProfile?.nickName + getString(R.string.moreInfoFragmentBasicComment)

        //상단메뉴
        myInfoIcon = view.findViewById(R.id.moreInfoFragmentMyInfo)
        myInfoIcon?.setOnClickListener(this)

        //하단 메뉴
        noticeTextView = view.findViewById(R.id.moreInfoFragmentNoticeTextView)
        noticeTextView?.setOnClickListener(this)

        eventTextView = view.findViewById(R.id.moreInfoFragmentEventTextView)
        eventTextView?.setOnClickListener(this)

        guideTextView = view.findViewById(R.id.moreInfoFragmentGuideTextView)
        guideTextView?.setOnClickListener(this)

        personalQuestionTextView = view.findViewById(R.id.moreInfoFragmentPersonalQuestionTextView)
        personalQuestionTextView?.setOnClickListener(this)

        settingTextView = view.findViewById(R.id.moreInfoFragmentSettingTextView)
        settingTextView?.setOnClickListener(this)

        shareAppTextView = view.findViewById(R.id.moreInfoFragmentShareAppTextView)
        shareAppTextView?.setOnClickListener(this)

        return  view
    }

    override fun onClick(v: View?) {

        when(v?.id){
            // 상단 메뉴.
            R.id.moreInfoFragmentMyInfo -> {
                var intent = Intent(context, MyInfoActivity::class.java)
                intent.putExtra("userAllInfo",mainActivity?.userAllInfo)
                startActivityForResult(intent,MYINFO_ACTIVITY)
            }
            // 하단 메뉴들.
            R.id.moreInfoFragmentNoticeTextView -> {
                var intent = Intent(context, NoticeBoardActivity::class.java)
                startActivity(intent)
            }
            R.id.moreInfoFragmentEventTextView -> {
                var intent = Intent(context, EventBoardActivity::class.java)
                startActivity(intent)
            }
            R.id.moreInfoFragmentGuideTextView -> {
                var intent = Intent(context, GuideActivity::class.java)
                startActivity(intent)
            }
            R.id.moreInfoFragmentPersonalQuestionTextView -> {
                sendEmail()
            }
            R.id.moreInfoFragmentSettingTextView -> {}
            R.id.moreInfoFragmentShareAppTextView -> {}
        }
    }

    private fun sendEmail() {
        //관리자 이메일 설정.
        val address = arrayOf(getString(R.string.adminEmail))
        //암시적 인텐트 생성.
        val emailIntent = Intent(Intent.ACTION_SENDTO)
            .setType("text/html")
            .setData(Uri.parse("mailto:"))
            .putExtra(Intent.EXTRA_EMAIL, address) // 임시
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.emailSubject))
        // 사용자 앱에 이메일을 보낼수 있는 앱을 찾아서 실행하고 없으면 경고 메시지.
        if (emailIntent.resolveActivity(mainActivity?.packageManager) != null)
            startActivity(emailIntent)
        else
            Toast.makeText(context, getString(R.string.emailAlter), Toast.LENGTH_SHORT).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            MYINFO_ACTIVITY ->{
                if(resultCode == Activity.RESULT_OK)
                    mainActivity?.userAllInfo = data?.getSerializableExtra("userAllInfo") as HashMap<String, Any>
                mainActivity?.updateData()
                basicComment!!.text =  mainActivity!!.userProfile?.nickName + getString(R.string.moreInfoFragmentBasicComment)
                // Test 토스트 메시지
                // Toast.makeText(context,mainActivity!!.userAddress?.cityProvince + mainActivity!!.userAddress?.district,Toast.LENGTH_SHORT).show()
                // Toast.makeText(context,mainActivity!!.userSchool?.name + mainActivity!!.userSchool?.major+mainActivity!!.userSchool?.track+mainActivity!!.userSchool?.status,Toast.LENGTH_SHORT).show()
                // Toast.makeText(context,"${mainActivity!!.userGrade?.totalAverageGrade}, ${mainActivity!!.userGrade?.totalPercentage}," +
                //        " ${mainActivity!!.userGrade?.lastAverageGrade}, ${mainActivity!!.userGrade?.lastPercentage}",Toast.LENGTH_LONG).show()
            }

        }
    }
}
