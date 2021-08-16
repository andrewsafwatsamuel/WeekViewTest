package com.alamkanak.weekview.weekviewapplication

import android.graphics.RectF
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alamkanak.weekview.weekviewapplication.databinding.ActivityMainBinding
import com.alamkanak.weekview.weekviewapplication.lib.DateTimeInterpreter
import com.alamkanak.weekview.weekviewapplication.lib.MonthLoader
import com.alamkanak.weekview.weekviewapplication.lib.WeekView
import com.alamkanak.weekview.weekviewapplication.lib.WeekViewEvent
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), WeekView.EventClickListener,
    MonthLoader.MonthChangeListener,
    WeekView.EventLongPressListener,
    WeekView.EmptyViewLongPressListener,
    WeekView.EmptyViewClickListener {

    private lateinit var binding: ActivityMainBinding

    private val events: MutableList<WeekViewEvent> = arrayListOf()

    private val mWeekView by lazy { findViewById<WeekView>(R.id.weekView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        initWeekView()
        setupDateTimeInterpreter()
    }

    private fun initWeekView() = with(mWeekView) {
        goToToday()
        numberOfVisibleDays = 1

        columnGap = applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f)
        textSize = applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f)
        eventTextSize = applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f)

        setOnEventClickListener(this@MainActivity)
        monthChangeListener = this@MainActivity
        eventLongPressListener = this@MainActivity
        emptyViewLongPressListener = this@MainActivity
        emptyViewClickListener = this@MainActivity
    }

    private fun applyDimension(unit: Int, value: Float): Int = TypedValue
        .applyDimension(unit, value, resources.displayMetrics)
        .toInt()

    private fun setupDateTimeInterpreter(/*shortDate: Boolean*/) {
        binding.weekView.dateTimeInterpreter = object : DateTimeInterpreter {
            override fun interpretday(date: Calendar?): String {
                return "TODO()"
            }

            override fun interpretDate(date: Calendar): String? {
                val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
                var weekday = weekdayNameFormat.format(date.time)
                val format = SimpleDateFormat(" M/d", Locale.getDefault())

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                // if (shortDate) weekday = weekday[0].toString()
                return weekday.uppercase(Locale.getDefault()) + format.format(date.time)
            }

            override fun interpretTime(hour: Int): String? {
                return if (hour > 11) (hour - 12).toString() + " PM" else if (hour == 0) "12 AM" else "$hour AM"
            }
        }
    }

    override fun onEventClick(event: WeekViewEvent?, eventRect: RectF?) {
        Toast.makeText(this, "Clicked " + event!!.name, Toast.LENGTH_SHORT).show()
    }

    override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent> = events

    private var id = 0L
    override fun onEmptyViewClicked(time: Calendar) {
        Toast.makeText(this, "Empty view pressed: " + getEventTitle(time), Toast.LENGTH_SHORT)
            .show()

        var endTime = time.clone() as Calendar
        endTime.add(Calendar.HOUR, 1)
        //endTime[Calendar.MONTH] = newMonth - 1
        var event = WeekViewEvent(id++,getEventTitle(time), time, endTime,"")
        event.color = resources.getColor(R.color.event_color_01)
        events.add(event)
mWeekView.notifyDatasetChanged()
    }

    override fun onEventLongPress(event: WeekViewEvent?, eventRect: RectF?) {
        Toast.makeText(this, "Long pressed event: " + event!!.name, Toast.LENGTH_SHORT).show()
    }

    override fun onEmptyViewLongPress(time: Calendar) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT)
            .show()
    }

    private fun getEventTitle(time: Calendar): String {
        return String.format(
            "Event of %02d:%02d %s/%d",
            time[Calendar.HOUR_OF_DAY],
            time[Calendar.MINUTE], time[Calendar.MONTH] + 1, time[Calendar.DAY_OF_MONTH]
        )
    }
}
/*
var startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = 3
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        var endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR, 1)
        endTime[Calendar.MONTH] = newMonth - 1
        var event = WeekViewEvent(1, getEventTitle(startTime), startTime, endTime)
        event.color = resources.getColor(R.color.event_color_01)
        events.add(event)
* */