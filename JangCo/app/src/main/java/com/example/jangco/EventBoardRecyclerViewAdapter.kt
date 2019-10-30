package com.example.jangco

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.SimpleDateFormat
import java.util.*
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions





class EventBoardRecyclerViewAdapter(option: FirestoreRecyclerOptions<Event>, val context: Context):
    FirestoreRecyclerAdapter<Event, EventBoardRecyclerViewAdapter.EventViewHolder>(option)  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_event_board_recyclerview_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int, model: Event) {
        holder.bind(model)
    }


    inner class EventViewHolder(view :View): RecyclerView.ViewHolder(view){
        private var eventTitle: String? = null
        private var imageUrl: String? = null
        private val eventImage : ImageView = view.findViewById(R.id.eventImage)
        private val eventDate: TextView =  view.findViewById(R.id.eventDate)


        fun bind(model: Event) {
            eventTitle =  model.title
            imageUrl = model.imageUrl



            val requestOptions = RequestOptions()
               // .skipMemoryCache(true)    //캐시 사용 해제, Firebase 사용 시 느리기 때문에 사용 필수
               // .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_loading) // 로딩시.
                .error(R.drawable.ic_error) // 이미지 없을때.

            Glide.with(context)
                .load(imageUrl)
                .apply(requestOptions)
                .thumbnail(0.5f)
                .into(eventImage)



            val sdf = SimpleDateFormat("yyyy.MM.dd",  Locale.KOREA) // 포멧
            val date = Date()
            val currentDate = sdf.parse(sdf.format(date)) // 현재날짜.
            val eventEndDate =sdf.parse( sdf.format(model.endDate))// 종료날짜.
            val result  = currentDate.compareTo(eventEndDate) // 비교
            if (result <= 0)
                eventDate.text = sdf.format(model.startDate)+" "+context.getString(R.string.moreInfoFragmentEventTerm)+" "+sdf.format(model.endDate)
            else
                eventDate.text = context.getText(R.string.moreInfoFragmentEventEndMessage)

        }
    }
}