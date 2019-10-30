package com.example.jangco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_notice_board.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class NoticeBoardActivity : AppCompatActivity() {

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private  var noticeRef: CollectionReference = db.collection("NoticeBoard")
    private  var adapter: NoticeBoardRecyclerViewAdapter? = null
    private  var noticeRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_board)

        //공지사항 상태바 툴바 셋팅.
        window.statusBarColor = getColor(R.color.appMainColor)
        settingToolBar()
        // 리사이클러뷰 셋팅.
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        var query: Query = noticeRef.orderBy("rate",Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<Notice>()
            .setQuery(query, Notice::class.java)
            .build()

        adapter = NoticeBoardRecyclerViewAdapter(options, this)
        noticeRecyclerView = findViewById(R.id.noticeBoardActivityRecyclerView)
        noticeRecyclerView?.adapter = adapter
        noticeRecyclerView?.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    private fun settingToolBar(){
        setSupportActionBar(noticeBoardActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게

        var toolbar = layoutInflater.inflate(R.layout.activity_notice_board_toolbar, null)
        supportActionBar?.customView = toolbar

        var backButton = toolbar.findViewById<ImageView>(R.id.noticeBoardToolbarBackImageView)
        backButton.setOnClickListener{ view ->
            finish()
        }
    }
}
