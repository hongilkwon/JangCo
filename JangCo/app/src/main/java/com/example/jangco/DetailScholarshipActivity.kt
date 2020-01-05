package com.example.jangco

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_detail_scholarship.*
import kotlinx.android.synthetic.main.fragment_scholarship_recyclerview_item.*
import java.text.SimpleDateFormat
import java.util.*

class DetailScholarshipActivity : AppCompatActivity() {

    private var scholarship: ScholarShip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_scholarship)

        scholarship = intent.getSerializableExtra("scholarship") as ScholarShip

        detailScholarshipActivityUrlButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(scholarship?.detailInfoURL))
            startActivity(intent)
        }

        detailScholarshipActivityNameTextView.text = scholarship?.name

        if(scholarship?.tagList?.get(0) == true) {
            detailScholarshipActivityGradeTagTextView.setTextColor(resources.getColor(R.color.colorPastelPink))
            detailScholarshipActivityGradeTagTextView.typeface = Typeface.DEFAULT_BOLD
        }
        if(scholarship?.tagList?.get(1) == true) {
            detailScholarshipActivityLocalTagTextView.setTextColor(resources.getColor(R.color.colorPastelPurple))
            detailScholarshipActivityLocalTagTextView.typeface = Typeface.DEFAULT_BOLD
        }
        if(scholarship?.tagList?.get(2) == true) {
            detailScholarshipActivitySpecialTagTextView.setTextColor(resources.getColor(R.color.colorPastelBlue))
            detailScholarshipActivitySpecialTagTextView.typeface = Typeface.DEFAULT_BOLD
        }
        if(scholarship?.tagList?.get(3) == true) {
            detailScholarshipActivityIncomeTagTextView.setTextColor(resources.getColor(R.color.appMainColor))
            detailScholarshipActivityIncomeTagTextView.typeface = Typeface.DEFAULT_BOLD
        }

        var sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val startDate = sdf.parse(scholarship?.startDate)
        val endDate = sdf.parse(scholarship?.endDate)
        val dDay = (endDate.time - Date().time) / (24 * 60 * 60 * 1000)
        detailScholarshipActivityDDayTextView.text = String.format("D-%d", dDay)

        sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        detailScholarshipActivityDateTextView.text =
            String.format("%s ~ %s", sdf.format(startDate), sdf.format(endDate))

        detailScholarshipActivityBenefitTextView.text = String.format("최대 %d만원", scholarship?.benefit)
        detailScholarshipActivityDescriptionTextView.text = scholarship?.description
    }
}
