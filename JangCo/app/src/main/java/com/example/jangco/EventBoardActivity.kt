package com.example.jangco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_event_board.*



class EventBoardActivity : AppCompatActivity() {

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var noticeRef: CollectionReference = db.collection("EventBoard")
    private var adapter: EventBoardRecyclerViewAdapter? = null
    private var eventRecyclerView: RecyclerView? = null
    private val itemDecoration: ItemOffsetDecoration = ItemOffsetDecoration(20)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_board)
        //Event 상태바 툴바 셋팅.
        window.statusBarColor = getColor(R.color.appMainColor)
        settingToolBar()
        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        var query: Query = noticeRef.orderBy("endDate", Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions.Builder<Event>()
            .setQuery(query, Event::class.java)
            .build()
        adapter = EventBoardRecyclerViewAdapter(options, this)
        eventRecyclerView = findViewById(R.id.eventBoardActivityRecyclerView)
        eventRecyclerView?.adapter = adapter
        eventRecyclerView?.addItemDecoration(itemDecoration)

    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }


    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }


    private fun settingToolBar() {
        setSupportActionBar(eventBoardActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게

        var toolbar = layoutInflater.inflate(R.layout.activity_event_board_toolbar,null)
        supportActionBar?.customView = toolbar

        var backButton = toolbar.findViewById<ImageView>(R.id.eventBoardToolbarBackImageView)
        backButton.setOnClickListener { view ->
            finish()
        }
    }
}
