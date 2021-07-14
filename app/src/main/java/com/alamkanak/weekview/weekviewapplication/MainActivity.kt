package com.alamkanak.weekview.weekviewapplication

import android.graphics.RectF
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alamkanak.weekview.weekviewapplication.lib.DateTimeInterpreter
import com.alamkanak.weekview.weekviewapplication.lib.MonthLoader
import com.alamkanak.weekview.weekviewapplication.lib.WeekView
import com.alamkanak.weekview.weekviewapplication.lib.WeekViewEvent
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), WeekView.EventClickListener,
    MonthLoader.MonthChangeListener,
    WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {

    lateinit var mWeekView: WeekView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mWeekView = findViewById(R.id.weekView)
        mWeekView.goToToday()

            mWeekView.numberOfVisibleDays = 1

            // Lets change some dimensions to best fit the view.
            mWeekView.columnGap =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics)
                    .toInt()
            mWeekView.textSize =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics)
                    .toInt()
            mWeekView.eventTextSize =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics)
                    .toInt()


        // Get a reference for the week view in the layout.

        // Get a reference for the week view in the layout.
        mWeekView = findViewById(R.id.weekView) as WeekView

        // Show a toast message about the touched event.

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this)

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this)

        // Set long press listener for events.

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this)

        // Set long press listener for empty view

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this)

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false)

        mWeekView.setEmptyViewClickListener {
            val time = it.time
            Toast.makeText(this,"month:${it.get(Calendar.MONTH)+1},day:${time.day}, hour:${time.hours},minutes:${time.minutes}",Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupDateTimeInterpreter(shortDate: Boolean) {
        mWeekView.dateTimeInterpreter = object : DateTimeInterpreter {
            override fun interpretDate(date: Calendar): String? {
                val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
                var weekday = weekdayNameFormat.format(date.time)
                val format = SimpleDateFormat(" M/d", Locale.getDefault())

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate) weekday = weekday[0].toString()
                return weekday.toUpperCase() + format.format(date.time)
            }

            override fun interpretTime(hour: Int): String? {
                return if (hour > 11) (hour - 12).toString() + " PM" else if (hour == 0) "12 AM" else "$hour AM"
            }
        }
    }

    override fun onEventClick(event: WeekViewEvent?, eventRect: RectF?) {
        Toast.makeText(this, "Clicked " + event!!.name, Toast.LENGTH_SHORT).show()
    }

    override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent> {
        // Populate the week view with some events.

        // Populate the week view with some events.
        val events: MutableList<WeekViewEvent> = ArrayList()

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

        startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = 3
        startTime[Calendar.MINUTE] = 30
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime[Calendar.HOUR_OF_DAY] = 4
        endTime[Calendar.MINUTE] = 30
        endTime[Calendar.MONTH] = newMonth - 1
        event = WeekViewEvent(10, getEventTitle(startTime), startTime, endTime)
        event.color = resources.getColor(R.color.event_color_02)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = 4
        startTime[Calendar.MINUTE] = 20
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime[Calendar.HOUR_OF_DAY] = 5
        endTime[Calendar.MINUTE] = 0
        event = WeekViewEvent(10, getEventTitle(startTime), startTime, endTime)
        event.color = resources.getColor(R.color.event_color_03)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = 5
        startTime[Calendar.MINUTE] = 30
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 2)
        endTime[Calendar.MONTH] = newMonth - 1
        event = WeekViewEvent(2, getEventTitle(startTime), startTime, endTime)
        event.color = resources.getColor(R.color.event_color_02)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = 5
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        startTime.add(Calendar.DATE, 1)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        endTime[Calendar.MONTH] = newMonth - 1
        event = WeekViewEvent(3, getEventTitle(startTime), startTime, endTime)
        event.color = resources.getColor(R.color.event_color_03)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_MONTH] = 15
        startTime[Calendar.HOUR_OF_DAY] = 3
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        event = WeekViewEvent(4, getEventTitle(startTime), startTime, endTime)
        event.color = resources.getColor(R.color.event_color_04)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_MONTH] = 1
        startTime[Calendar.HOUR_OF_DAY] = 3
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        event = WeekViewEvent(5, getEventTitle(startTime), startTime, endTime)
        event.color = resources.getColor(R.color.event_color_01)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_MONTH] = startTime.getActualMaximum(Calendar.DAY_OF_MONTH)
        startTime[Calendar.HOUR_OF_DAY] = 15
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        event = WeekViewEvent(5, getEventTitle(startTime), startTime, endTime)
        event.color = resources.getColor(R.color.event_color_02)
        events.add(event)

        //AllDay event

        //AllDay event
        startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = 0
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 23)
        event = WeekViewEvent(7, getEventTitle(startTime), null, startTime, endTime, true)
        event.color = resources.getColor(R.color.event_color_04)
        events.add(event)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_MONTH] = 8
        startTime[Calendar.HOUR_OF_DAY] = 2
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime[Calendar.DAY_OF_MONTH] = 10
        endTime[Calendar.HOUR_OF_DAY] = 23
        event = WeekViewEvent(8, getEventTitle(startTime), null, startTime, endTime, true)
        event.color = resources.getColor(R.color.event_color_03)
        events.add(event)

        // All day event until 00:00 next day

        // All day event until 00:00 next day
        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_MONTH] = 10
        startTime[Calendar.HOUR_OF_DAY] = 0
        startTime[Calendar.MINUTE] = 0
        startTime[Calendar.SECOND] = 0
        startTime[Calendar.MILLISECOND] = 0
        startTime[Calendar.MONTH] = newMonth - 1
        startTime[Calendar.YEAR] = newYear
        endTime = startTime.clone() as Calendar
        endTime[Calendar.DAY_OF_MONTH] = 11
        event = WeekViewEvent(8, getEventTitle(startTime), null, startTime, endTime, true)
        event.color = resources.getColor(R.color.event_color_01)
        events.add(event)

        return events
    }

    override fun onEventLongPress(event: WeekViewEvent?, eventRect: RectF?) {
        Toast.makeText(this, "Long pressed event: " + event!!.name, Toast.LENGTH_SHORT).show()    }

    override fun onEmptyViewLongPress(time: Calendar) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT)
            .show()
    }
    protected fun getEventTitle(time: Calendar): String {
        return String.format(
            "Event of %02d:%02d %s/%d",
            time[Calendar.HOUR_OF_DAY],
            time[Calendar.MINUTE], time[Calendar.MONTH] + 1, time[Calendar.DAY_OF_MONTH]
        )
    }
}