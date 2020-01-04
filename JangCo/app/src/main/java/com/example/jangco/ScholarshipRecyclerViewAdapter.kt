package com.example.jangco

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class ScholarshipRecyclerViewAdapter(
    val context: Context,
    val userProfile: User,
    val allScholarShip: ArrayList<ScholarShip>,
    val myScholarShipList: ArrayList<ScholarShip>)
    : RecyclerView.Adapter<ScholarshipRecyclerViewAdapter.ScholarshipViewHolder>() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var dataBaseHelper: DataBaseHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScholarshipViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_scholarship_recyclerview_item, parent, false)

        dataBaseHelper = DataBaseHelper(firebaseAuth.currentUser?.email!!)

        return ScholarshipViewHolder(view)
    }

    override fun getItemCount(): Int {
       return allScholarShip.size
    }

    override fun onBindViewHolder(holder: ScholarshipViewHolder, position: Int) {
        holder.bind(position)
    }



    inner class ScholarshipViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val scholarType = view.findViewById<View>(R.id.scholarType)
        private val scholarName = view.findViewById<TextView>(R.id.scholarName)
        private val scholarBenefit = view.findViewById<TextView>(R.id.scholarBenefit)
        private val scholarDDay = view.findViewById<TextView>(R.id.scholarDDay)
        private val scholarEndDate = view.findViewById<TextView>(R.id.scholarEndDate)
        private val scholarGradeTag = view.findViewById<TextView>(R.id.scholarGradeTag)
        private val scholarLocalTag = view.findViewById<TextView>(R.id.scholarLocalTag)
        private val scholarSpecialTag = view.findViewById<TextView>(R.id.scholarSpecialTag)
        private val scholarIncomeTag = view.findViewById<TextView>(R.id.scholarIncomeTag)
        private val scholarCalendarImageView = view.findViewById<ImageView>(R.id.scholarCalendarImageView)

        fun bind(position: Int) {

            var model = allScholarShip.get(position)
            // type 별 색상 설정
            when(model.type) {
                "장학금" -> {
                    scholarType.setBackgroundColor(context.resources.getColor(R.color.colorPastelBlue))
                }
                "학자금" -> {
                    scholarType.setBackgroundColor(context.resources.getColor(R.color.colorPastelGreen))
                }
                "기숙사" -> {
                    scholarType.setBackgroundColor(context.resources.getColor(R.color.colorPastelPink))
                }
                else -> {
                    scholarType.setBackgroundColor(context.resources.getColor(R.color.colorPastelPurple))
                }
            }
            // 장학금 기본 데이터
            scholarName.text = model.name
            scholarBenefit.text = String.format("최대 %s만원", model.benefit)

            var sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val endDate = sdf.parse(model.endDate)
            val dDay = (endDate.time - Date().time) / (24 * 60 * 60 * 1000)
            scholarDDay.text = String.format("D-%d", dDay)

            sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            scholarEndDate.text = sdf.format(endDate)

            if(model.tagList?.get(0) == true) {
                scholarGradeTag.setTextColor(context.resources.getColor(R.color.colorPastelPink))
                scholarGradeTag.typeface = Typeface.DEFAULT_BOLD
            }
            if(model.tagList?.get(1) == true) {
                scholarLocalTag.setTextColor(context.resources.getColor(R.color.colorPastelPurple))
                scholarLocalTag.typeface = Typeface.DEFAULT_BOLD
            }
            if(model.tagList?.get(2) == true) {
                scholarSpecialTag.setTextColor(context.resources.getColor(R.color.colorPastelBlue))
                scholarSpecialTag.typeface = Typeface.DEFAULT_BOLD
            }
            if(model.tagList?.get(3) == true) {
                scholarIncomeTag.setTextColor(context.resources.getColor(R.color.appMainColor))
                scholarIncomeTag.typeface = Typeface.DEFAULT_BOLD
            }

            if(userProfile.bookMarkMap?.containsKey(model.id)!!)
                scholarCalendarImageView.setImageResource(R.drawable.ic_calendar_checked)

            scholarCalendarImageView.setOnClickListener {
                if(userProfile.bookMarkMap?.containsKey(model.id)!!) {
                    userProfile.bookMarkMap?.remove(model.id)
                    myScholarShipList.remove(model)
                    scholarCalendarImageView.setImageResource(R.drawable.ic_calendar_unchecked)
                } else {
                    userProfile.bookMarkMap?.put(model.id!!, true)
                    myScholarShipList.add(model)
                    scholarCalendarImageView.setImageResource(R.drawable.ic_calendar_checked)
                }

                dataBaseHelper.updateBookmark(userProfile.bookMarkMap!!)
            }

        }
    }


}
