package com.example.jangco

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MyScholarShipRecyclerViewAdapter(
    private val myScholarShipList : List<ScholarShip>,
    private val context: Context
) :
    RecyclerView.Adapter<MyScholarShipRecyclerViewAdapter.MyScholarShipViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyScholarShipViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_my_scholarship_recyclerview_item, parent, false)

        return MyScholarShipViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return myScholarShipList.size
    }

    override fun onBindViewHolder(holder: MyScholarShipViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class MyScholarShipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val myScholarEndDate = itemView.findViewById<TextView>(R.id.myScholarEndDate)
        private val myScholarName = itemView.findViewById<TextView>(R.id.myScholarName)
        private val myScholarDDay = itemView.findViewById<TextView>(R.id.myScholarDDay)

        fun onBind(position: Int) {
            val scholarShip = myScholarShipList[position]

            var sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val endDate = sdf.parse(scholarShip.endDate)
            val dDay = (endDate.time - Date().time) / (24 * 60 * 60 * 1000)
            myScholarDDay.text = String.format("D-%d", dDay)

            sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            myScholarEndDate.text = sdf.format(endDate)

            myScholarName.text = scholarShip.name
        }
    }
}