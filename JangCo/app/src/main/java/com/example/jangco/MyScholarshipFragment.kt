package com.example.jangco


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import kotlinx.android.synthetic.main.calendar_day_layout.view.*
import kotlinx.android.synthetic.main.calendar_month_header.view.*
import kotlinx.android.synthetic.main.fragment_my_scholarship.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class MyScholarshipFragment : Fragment() {

    private var mainActivity: MainActivity? = null

    private var myScholarShipList: ArrayList<ScholarShip>? = null
    private var adapter: MyScholarShipRecyclerViewAdapter? = null
    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    private lateinit var myScholarshipFragmentRecyclerView: RecyclerView
    private lateinit var myScholarshipFragmentCalendarView: CalendarView
    private lateinit var myScholarshipFragmentNextMonthImageView: ImageView
    private lateinit var myScholarshipFragmentPreviousMonthImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_scholarship, container, false)

        mainActivity = activity as MainActivity
        myScholarShipList = mainActivity?.myScholarShipList

        myScholarshipFragmentRecyclerView = view.findViewById(R.id.myScholarshipFragmentRecyclerView)
        myScholarshipFragmentCalendarView = view.findViewById(R.id.myScholarshipFragmentCalendarView)
        myScholarshipFragmentNextMonthImageView = view.findViewById(R.id.myScholarshipFragmentNextMonthImageView)
        myScholarshipFragmentPreviousMonthImageView = view.findViewById(R.id.myScholarshipFragmentPreviousMonthImageView)

        myScholarshipFragmentRecyclerView.setHasFixedSize(true)
        adapter = MyScholarShipRecyclerViewAdapter(myScholarShipList!!, context!!)
        myScholarshipFragmentRecyclerView.adapter = adapter


        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        myScholarshipFragmentCalendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        myScholarshipFragmentCalendarView.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            val calendarDayText = view.calendarDayText
            val scheduleText = view.scheduleText
            val scheduleText2 = view.scheduleText2
            val scheduleLabel = view.scheduleLabel
            val layout = view.layout
            lateinit var day: CalendarDay

            init {
                selectedDate = LocalDate.now()
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            myScholarshipFragmentCalendarView.notifyDateChanged(day.date)
                            oldDate?.let { myScholarshipFragmentCalendarView.notifyDateChanged(it) }
                        }
                    }
                }
            }
        }

        myScholarshipFragmentCalendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val scholars = myScholarShipList?.groupBy { it.endDate }
                //Log.d("test", "$schedules")
                val calendarDayText = container.calendarDayText
                val scheduleText = container.scheduleText
                val scheduleText2 = container.scheduleText2
                val scheduleLabel = container.scheduleLabel
                val layout = container.layout

                calendarDayText.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    when(day.date.dayOfWeek.value) {
                        7 -> {
                            val color = resources.getColor(android.R.color.holo_red_light)
                            calendarDayText.setTextColor(color)
                        }
                        6 -> {
                            val color = resources.getColor(android.R.color.holo_blue_dark)
                            calendarDayText.setTextColor(color)
                        }
                        else -> {
                            val color = resources.getColor(android.R.color.black)
                            calendarDayText.setTextColor(color)
                        }
                    }

                    layout.setBackgroundResource(
                        if (selectedDate == day.date) R.drawable.calendar_day_selected else 0
                    )
                } else {
                    calendarDayText.setTextColor(Color.GRAY)
                }

                val scholar = scholars?.get(dateFormatter.format(day.date))
                if(scholar != null) {
                    when(scholar.count()) {
                        1 -> {
                            scheduleText.text = scholar[0].name
                            scheduleText2.text = ""
                        }
                        2 -> {
                            scheduleText2.visibility = View.VISIBLE
                            scheduleLabel.visibility = View.GONE

                            scheduleText.text = scholar[0].name
                            scheduleText2.text = scholar[1].name
                        }
                        else -> {
                            val num = scholar.count() - 1
                            scheduleText2.visibility = View.GONE
                            scheduleLabel.visibility = View.VISIBLE

                            scheduleText.text = scholar[0].name
                            scheduleText2.text = scholar[1].name
                            scheduleLabel.text = "+$num"
                        }
                    }
                } else {
                    scheduleText.text = ""
                    scheduleText2.text = ""
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = view.calendarMonthHeaderLayout
        }
        myScholarshipFragmentCalendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                }
            }
        }

        myScholarshipFragmentCalendarView.monthScrollListener = { month ->
            val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
            myScholarshipFragmentMonthYearText.text = title

            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                myScholarshipFragmentCalendarView.notifyDateChanged(it)
            }
        }

        myScholarshipFragmentNextMonthImageView.setOnClickListener {
            myScholarshipFragmentCalendarView.findFirstVisibleMonth()?.let {
                myScholarshipFragmentCalendarView.smoothScrollToMonth(it.yearMonth.next)
            }
        }

        myScholarshipFragmentPreviousMonthImageView.setOnClickListener {
            myScholarshipFragmentCalendarView.findFirstVisibleMonth()?.let {
                myScholarshipFragmentCalendarView.smoothScrollToMonth(it.yearMonth.previous)
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
    }

}
