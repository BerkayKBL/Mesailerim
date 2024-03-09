package com.berkaykbl.mesailerim

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.berkaykbl.mesailerim.Adapters.DateAdapter
import com.berkaykbl.mesailerim.Adapters.DateItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Timer
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {

    private var gridView: GridView? = null
    private var arrayList: ArrayList<DateItem>? = null
    private var dateAdapter: DateAdapter? = null
    private var month: Int = 0
    private var year: Int = 0
    private var currentDate: String = ""

    fun changeDate(dateFormat: DateFormat) {
        val datee = Date()
        val date = dateFormat.format(datee)
        val cal = Calendar.getInstance()
        month = date.split("/")[1].toInt()
        year = date.split("/")[2].toInt()

        val monthName = when(month) {
            1-> "Ocak"
            2-> "Şubat"
            3-> "Mart"
            4-> "Nisan"
            5-> "Mayıs"
            6-> "Haziran"
            7-> "Temmuz"
            8-> "Ağustos"
            9-> "Eylül"
            10-> "Ekim"
            11-> "Kasım"
            12-> "Aralık"
            else -> "? ?"
        }
        cal.set(year, month - 1, 0)
        /*
                val cal: Calendar = Calendar.getInstance()
                cal.clear()
                cal.set(2024, 2 - 1, 1)
                val daysInMonth: Int = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
                for (i in 0 until daysInMonth) {
                    System.out.println(fmt.format(cal.getTime()))
                    cal.add(Calendar.DAY_OF_MONTH, 1)
                }*/


        arrayList =  setDataList(date, cal.get(Calendar.DAY_OF_WEEK) - 1)
        dateAdapter = DateAdapter(applicationContext, arrayList!!)
        gridView?.adapter = dateAdapter

        gridView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Log.d("qweqw", "qewqweqweqwe")
            Toast.makeText(applicationContext, "test", Toast.LENGTH_SHORT).show()
        }
        val monthNameView = findViewById<TextView>(R.id.monthName)

        monthNameView.text = year.toString() + " "+monthName
    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        var dateFormat: DateFormat = SimpleDateFormat("d/M/YYYY")
        val datee = Date()
        currentDate =  dateFormat.format(datee)
        gridView = findViewById(R.id.calendar_grid)
        val da = findViewById<TextView>(R.id.daaa)
        changeDate(dateFormat)

        val prevMonth = findViewById<ImageButton>(R.id.previousMonth)
        val nextMonth = findViewById<ImageButton>(R.id.nextMonth)

        prevMonth.setOnClickListener {
            if (month > 0) {
                var m = month
                var y = year
                if (month == 1) {
                    m = 12
                    y = year - 1
                } else {
                    m -= 1
                }
                dateFormat= SimpleDateFormat("d/"+m+"/"+y)
                changeDate(dateFormat)
            }
        }

        nextMonth.setOnClickListener {
            if (month > 0) {
                var m = month
                var y = year
                if (month == 12) {
                    m = 1
                    y = year + 1
                } else {
                    m += 1
                }
                dateFormat= SimpleDateFormat("d/"+m+"/"+y)
                changeDate(dateFormat)
            }
        }

    }

    public fun setDataList(date: String, firstDay: Int): ArrayList<DateItem> {
        var arrayList: ArrayList<DateItem> = ArrayList()
        while (arrayList.size < firstDay) {
            arrayList.add(DateItem("", false, false, false, 0.0, false))
        }
        val day: Int = date.split("/")[0].toInt()
        val cal: Calendar = Calendar.getInstance()
        val fmt = SimpleDateFormat("d")
        val fmt2 = SimpleDateFormat("d/M/YYYY")
        cal.clear()
        cal.set(year, month - 1, 1)
        val daysInMonth: Int = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 0 until daysInMonth) {
            val currentDate = fmt.format(cal.getTime())
            val currentDate2 = fmt2.format(cal.getTime())
            var isToday = currentDate2 == this.currentDate
            val isSunday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
            arrayList.add(DateItem(currentDate, isSunday, isToday, false, 0.0, false))
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return arrayList
    }
}