package com.example.jangco

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.SimpleDateFormat
import java.util.*

class NoticeBoardRecyclerViewAdapter(option: FirestoreRecyclerOptions<Notice>, val context: Context):
    FirestoreRecyclerAdapter<Notice, NoticeBoardRecyclerViewAdapter.NoticeViewHolder>(option) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_notice_board_recyclerview_item, parent, false)
        return NoticeViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int, model: Notice) {
        holder.bind(model)
    }

    inner class NoticeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val noticeRate = view.findViewById<TextView>(R.id.noticeRate)
        private val noticeTitle = view.findViewById<TextView>(R.id.noticeTitle)
        private val noticeDate = view.findViewById<TextView>(R.id.noticeDate)
        private val noticeContents = view.findViewById<TextView>(R.id.noticeContents)

        private var noticeExtend = view.findViewById<ImageView>(R.id.noticeExtend)
        private var switch: Int? = 0

        fun bind(model: Notice) {
            // 1이면 필독 공지 2이면 일반 공지
            if( model.rate?.toInt() == 1)
                noticeRate.text = context.getString(R.string.moreInfoFragmentNoticeMustReadRate)
            else if( model.rate?.toInt() == 2){
                noticeRate.text = context.getString(R.string.moreInfoFragmentNoticeBasicRate)
                noticeRate.setTextColor(context.getColor(R.color.colorGray))
            }
            noticeTitle.text = model.title

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            noticeDate.text = sdf.format(model.date)
            noticeContents.text = model.contents

            noticeExtend.setOnClickListener { view ->
                if(switch == 0) {
                    noticeExtend.setImageResource(R.drawable.ic_up_arrow)
                    noticeContents.visibility = View.VISIBLE
                    switch = 1
                }
                else {
                    noticeExtend.setImageResource(R.drawable.ic_down_arrow)
                    noticeContents.visibility = View.GONE
                    switch = 0
                }
            }
        }

    }
}