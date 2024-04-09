package com.berkaykbl.mesailerim

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.room.migration.Migration
import com.berkaykbl.mesailerim.Adapters.DateAdapter
import com.berkaykbl.mesailerim.Adapters.DateItem
import com.berkaykbl.mesailerim.Utils.month
import com.berkaykbl.mesailerim.Utils.day
import com.berkaykbl.mesailerim.Utils.year
import com.berkaykbl.mesailerim.datedb.AppDatabase
import com.berkaykbl.mesailerim.datedb.DateDao
import com.berkaykbl.mesailerim.datedb.DateEntity
import com.berkaykbl.mesailerim.datedb.SettingsDao
import com.berkaykbl.mesailerim.datedb.SettingsEntity
import com.berkaykbl.mesailerim.dialog.AddExtraInterrupt
import com.berkaykbl.mesailerim.dialog.CallbackExtraInterrupt
import com.berkaykbl.mesailerim.dialog.CallbackShift
import com.berkaykbl.mesailerim.dialog.CallbackVacation
import com.berkaykbl.mesailerim.dialog.CallbackWage
import com.berkaykbl.mesailerim.dialog.ChangeShift
import com.berkaykbl.mesailerim.dialog.ChangeVacation
import com.berkaykbl.mesailerim.dialog.ChangeWage
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class MainActivity : AppCompatActivity(), CallbackShift, CallbackVacation, CallbackWage,
    CallbackExtraInterrupt {

    private var arrayList: ArrayList<DateItem>? = null
    private var dateAdapter: DateAdapter? = null
    private var currentDate: String = ""
    private var lastView: View? = null
    private var utils: Utils = Utils
    private var db: AppDatabase? = null
    private var dateDao: DateDao? = null
    private var settingsDao: SettingsDao? = null
    private var lifecycleMonthly: ArrayList<Job> = ArrayList()
    private var isLifecycleSet = false
    private var lifecycleYearly: ArrayList<Job> = ArrayList()

    fun changeDate(dateFormat: DateFormat, gridView: GridView) {
        val datee = Date()
        val date = dateFormat.format(datee)
        val cal = Calendar.getInstance()
        val newmonth = date.split("/")[1].toInt()
        val newyear = date.split("/")[2].toInt()
        if (newmonth != month) {
            day = 0
        } else day = utils.day
        month = newmonth
        year = newyear

        val monthName = when (month) {
            1 -> "Ocak"
            2 -> "Şubat"
            3 -> "Mart"
            4 -> "Nisan"
            5 -> "Mayıs"
            6 -> "Haziran"
            7 -> "Temmuz"
            8 -> "Ağustos"
            9 -> "Eylül"
            10 -> "Ekim"
            11 -> "Kasım"
            12 -> "Aralık"
            else -> "? ?"
        }
        cal.set(utils.year, utils.month - 1, 0)
        /*
                val cal: Calendar = Calendar.getInstance()
                cal.clear()
                cal.set(2024, 2 - 1, 1)
                val daysInMonth: Int = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
                for (i in 0 until daysInMonth) {
                    System.out.println(fmt.format(cal.getTime()))
                    cal.add(Calendar.DAY_OF_MONTH, 1)
                }*/


        arrayList = setDataList(date, cal.get(Calendar.DAY_OF_WEEK) - 1)
        dateAdapter = DateAdapter(applicationContext, arrayList!!)
        gridView.adapter = dateAdapter

        val monthNameView = findViewById<TextView>(R.id.monthName)

        monthNameView.text = utils.year.toString() + " " + monthName


        gridView.onItemClickListener = AdapterView.OnItemClickListener { p, v, po, i ->
            val dayText = p[po].findViewById<TextView>(R.id.day).text.toString()
            if (dayText.isNotEmpty()) {
                if (lastView == null) {
                    lastView = p[po]
                    lastView!!.isSelected = true
                } else {
                    lastView!!.isSelected = false
                    lastView = p[po]
                    lastView!!.isSelected = true

                }
                val dayView = findViewById<TextView>(R.id.rightDate)
                day = dayText.toInt()
                dayView.text = String.format("%s/%s/%s", day.toString(), month, year)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)


        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "dates-database")
            .allowMainThreadQueries().build()

        dateDao = db!!.dateDao()


        settingsDao = db!!.settingsDao()
        utils.monthDB = dateDao!!.getDateWithMonth(month, year)
        var dateFormat: DateFormat = SimpleDateFormat("d/M/YYYY")
        val datee = Date()
        currentDate = dateFormat.format(datee)
        val gridView = findViewById<GridView>(R.id.calendar_grid)

        val date = dateFormat.format(datee)
        val cal = Calendar.getInstance()
        month = date.split("/")[1].toInt()
        year = date.split("/")[2].toInt()
        changeDate(dateFormat, gridView)

        val monthName = when (month) {
            1 -> "Ocak"
            2 -> "Şubat"
            3 -> "Mart"
            4 -> "Nisan"
            5 -> "Mayıs"
            6 -> "Haziran"
            7 -> "Temmuz"
            8 -> "Ağustos"
            9 -> "Eylül"
            10 -> "Ekim"
            11 -> "Kasım"
            12 -> "Aralık"
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


        val monthNameView = findViewById<TextView>(R.id.monthName)

        monthNameView.text = String.format("%s %s", year.toString(), monthName)


        val prevMonth = findViewById<ImageButton>(R.id.previousMonth)
        val nextMonth = findViewById<ImageButton>(R.id.nextMonth)

        prevMonth.setOnClickListener {
            if (month > 0) {
                var m = month
                var y = year
                if (month == 1) {
                    m = 12
                    y = year - 1
                    lifecycleYearly.forEach { it.cancel() }
                    setlifecycleYearly(m, y, gridView)
                } else {
                    m -= 1
                }
                lifecycleMonthly.forEach { it.cancel() }
                setlifecycleMonthly(m, y, gridView)
            }
        }

        nextMonth.setOnClickListener {
            if (month > 0) {
                var m = month
                var y = year
                if (month == 12) {
                    m = 1
                    y = year + 1

                    lifecycleYearly.forEach { it.cancel() }
                    setlifecycleYearly(m, y, gridView)
                } else {
                    m += 1
                }
                lifecycleMonthly.forEach { it.cancel() }
                setlifecycleMonthly(m, y, gridView)
            }
        }


        val enterShiftButton = findViewById<Button>(R.id.enterShiftButton)
        val enterVacationButton = findViewById<Button>(R.id.enterVacationButton)
        val enterExtraButton = findViewById<Button>(R.id.enterExtraButton)
        val enterInterruptionButton = findViewById<Button>(R.id.enterInterruptionButton)
        val deleteMontlyDataButton = findViewById<Button>(R.id.deleteMontlyDataButton)
        val changeWage = findViewById<Button>(R.id.changeWage)

        enterShiftButton.setOnClickListener {
            if (day != 0 && year != 0 && month != 0) {

                val dialog: DialogFragment =
                    ChangeShift(0, String.format("%s/%s/%s", day, month, year))
                dialog.show(supportFragmentManager, "")
            }
        }

        enterExtraButton.setOnClickListener {
            if (year != 0 && month != 0) {
                val dialog: DialogFragment =
                    AddExtraInterrupt(0, String.format("%s_%s", month, year), "extra")
                dialog.show(supportFragmentManager, "")
            }
        }
        enterInterruptionButton.setOnClickListener {
            if (year != 0 && month != 0) {
                val dialog: DialogFragment =
                    AddExtraInterrupt(0, String.format("%s_%s", month, year), "interrupt")
                dialog.show(supportFragmentManager, "")
            }
        }
        enterVacationButton.setOnClickListener {
            if (day != 0 && year != 0 && month != 0) {

                val dialog: DialogFragment =
                    ChangeVacation("", String.format("%s/%s/%s", day, month, year))
                dialog.show(supportFragmentManager, "")
            }
        }

        changeWage.setOnClickListener {

            val dialog: DialogFragment = ChangeWage("", year)
            dialog.show(supportFragmentManager, "")
        }

        deleteMontlyDataButton.setOnClickListener {
            if (year != 0 && month != 0) {
                dateDao!!.deleteMontlyData(month, year)
            }
        }

        if (!isLifecycleSet) {
            setlifecycleMonthly(month, year, gridView)
            setlifecycleYearly(month, year, gridView)
            isLifecycleSet = true

        }


    }

    fun setlifecycleYearly(month: Int, year: Int, gridView: GridView) {
        val lifecycleYearly = ArrayList<Job>()
        lifecycleYearly.add(lifecycleScope.launch {
            settingsDao!!.getSettingWithListener("wage_" + year).collect { wage ->

                if (wage.isEmpty())  {
                    utils.wage = 0
                    return@collect
                }
                utils.wage = wage.first().value.toInt()
                val dateFormat = SimpleDateFormat("d/" + month + "/" + year)
                changeDate(dateFormat, gridView)
            }
        })
        this.lifecycleYearly = lifecycleYearly
    }

    fun setlifecycleMonthly(month: Int, year: Int, gridView: GridView) {
        val lifecycleMonthly = ArrayList<Job>()
        lifecycleMonthly.add(
            lifecycleScope.launch {
                dateDao!!.getDateWithMonthWithListener(month, year).collect { datese ->

                    utils.monthDB = datese
                    val dateFormat = SimpleDateFormat("d/" + month + "/" + year)
                    changeDate(dateFormat, gridView)
                }
            }
        )
        lifecycleMonthly.add(
            lifecycleScope.launch {
                settingsDao!!.getSettingWithListener("extra_" + month + "_" + year)
                    .collect { extra ->

                        if (extra.isEmpty()) {
                            utils.extra = 0
                            return@collect
                        }
                        utils.extra = extra.first().value.toInt()
                        val dateFormat = SimpleDateFormat("d/" + month + "/" + year)
                        changeDate(dateFormat, gridView)
                    }

            }
        )
        lifecycleMonthly.add(
            lifecycleScope.launch {
                settingsDao!!.getSettingWithListener("interrupt_" + month + "_" + year)
                    .collect { interrupt ->

                        if (interrupt.isEmpty()) {
                            utils.interrupt = 0
                            return@collect
                        }
                        utils.interrupt = interrupt.first().value.toInt()
                        val dateFormat = SimpleDateFormat("d/" + month + "/" + year)
                        changeDate(dateFormat, gridView)
                    }
            }
        )
        this.lifecycleMonthly = lifecycleMonthly
    }

    fun setWageLifecycle() {

    }

    fun setDataList(date: String, firstDay: Int): ArrayList<DateItem> {
        var arrayList: ArrayList<DateItem> = ArrayList()
        while (arrayList.size < firstDay) {
            arrayList.add(DateItem("", false, false, 0, 0))
        }
        val day: Int = date.split("/")[0].toInt()
        val cal: Calendar = Calendar.getInstance()
        val fmt = SimpleDateFormat("d")
        val fmt2 = SimpleDateFormat("d/M/YYYY")
        cal.clear()
        cal.set(year, month - 1, 1)
        val daysInMonth: Int = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        var midWeekShiftVal = 0
        var saturdayShiftVal = 0
        var sundayShiftVal = 0
        var totalShiftVal = 0
        var paidVacationVal = 0
        var unpaidVacationVal = 0
        var reportVacationVal = 0
        var feeInterruptedDay = 0

        var dayWeek = 0
        for (i in 0 until daysInMonth) {
            val mont =
                utils.monthDB.find { it.day == i + 1 && it.year == year && it.month == month }
            var isPermit = 0
            var shift = 0
            if (mont != null) {
                isPermit = mont.isPermit
                shift = mont.shift
            }
            val currentDate = fmt.format(cal.getTime())
            val currentDate2 = fmt2.format(cal.getTime())
            var isToday = currentDate2 == this.currentDate
            val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
            val isSunday = dayOfWeek == Calendar.SUNDAY
            if (isPermit != 0) {
                if (isPermit == 2) {
                    paidVacationVal += 1
                } else {
                    val dayWeek2 = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH)
                    if (isPermit == 1) {
                        unpaidVacationVal += 1
                        if (dayWeek2 == dayWeek) {
                            feeInterruptedDay += 1
                        } else {
                            feeInterruptedDay += 2
                        }
                    } else {
                        reportVacationVal += 1
                        feeInterruptedDay += 1
                    }
                    dayWeek = dayWeek2

                }
            } else {
                if (dayOfWeek == Calendar.SUNDAY) {
                    sundayShiftVal += shift
                    totalShiftVal += shift
                } else if (dayOfWeek == Calendar.SATURDAY) {
                    saturdayShiftVal += shift
                    totalShiftVal += shift
                } else {
                    midWeekShiftVal += shift
                    totalShiftVal += shift
                }
            }

            Log.d("qq", (utils.day == i + 1).toString())
            arrayList.add(DateItem(currentDate, isSunday, isToday, isPermit, shift, utils.day == i + 1))
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }

        val midweekShiftView = findViewById<TextView>(R.id.midweekShift)
        val saturdayShiftView = findViewById<TextView>(R.id.saturdayShift)
        val sundayShiftView = findViewById<TextView>(R.id.sundayShift)
        val totalShiftView = findViewById<TextView>(R.id.totalShift)
        midweekShiftView.text = midWeekShiftVal.toString()
        saturdayShiftView.text = saturdayShiftVal.toString()
        sundayShiftView.text = sundayShiftVal.toString()
        totalShiftView.text = totalShiftVal.toString()


        val paidVacationView = findViewById<TextView>(R.id.paidVacation)
        val unpaidVacationView = findViewById<TextView>(R.id.unpaidVacation)
        val reportVacationView = findViewById<TextView>(R.id.reportVacation)
        val feeInterruptedDayView = findViewById<TextView>(R.id.totalFeeInterruptedDay)
        paidVacationView.text = paidVacationVal.toString()
        unpaidVacationView.text = unpaidVacationVal.toString()
        reportVacationView.text = reportVacationVal.toString()
        feeInterruptedDayView.text = feeInterruptedDay.toString()

        val shiftWageView = findViewById<TextView>(R.id.shiftWage)
        val newWage =
            calculateWage(midWeekShiftVal, saturdayShiftVal, sundayShiftVal, feeInterruptedDay)
        shiftWageView.text = newWage


        return arrayList
    }

    private fun calculateWage(
        midweekShift: Int,
        saturdayShift: Int,
        sundayShift: Int,
        feeInterruptedDay: Int
    ): String {
        val wage = utils.wage
        val extra = utils.extra
        val interrupt = utils.interrupt
        val dailyWage = wage / 30
        val hoursWage = dailyWage / 7.5
        val midweekShiftMultiple = 1.5
        val saturdayShiftMultiple = 1.5
        val sundayShiftMultiple = 2
        val midweekShiftFee = midweekShift * midweekShiftMultiple * hoursWage
        val saturdayShiftFee = saturdayShift * saturdayShiftMultiple * hoursWage
        val sundayShiftFee = sundayShift * sundayShiftMultiple * hoursWage
        Log.d("extra", extra.toString())
        val newWage =
            wage + (midweekShiftFee + saturdayShiftFee + sundayShiftFee) - (dailyWage * feeInterruptedDay) + (extra - interrupt)


        val decimalFormat = DecimalFormat("#,###.##")
        decimalFormat.applyPattern("#,##0.##")
        return decimalFormat.format(newWage)
    }

    override fun onShiftChange(newValue: String, date: String) {
        val dateDB = dateDao!!.getDate(day, month, year)
        var newSh: Int = 0
        if (newValue.isEmpty()) {
            newSh = 0
        } else {
            newSh = newValue.toInt()
        }
        if (dateDB.isEmpty()) {
            dateDao!!.insertDate(
                DateEntity(
                    day = day,
                    month = month,
                    year = year,
                    isPermit = 0,
                    shift = newSh
                )
            )
        } else {
            val newDate = dateDB.first().copy()
            newDate.shift = newSh
            dateDao!!.update(newDate)
        }
    }

    override fun onWageChange(newValue: Int, year: Int) {

        val setDB = settingsDao!!.getSetting("wage_" + year)

        if (setDB.isEmpty()) {
            settingsDao!!.insertSetting(
                SettingsEntity(
                    key = "wage_" + year,
                    value = newValue.toString()
                )
            )
        } else {

            val newSet = setDB.first()
            newSet.value = newValue.toString()
            settingsDao!!.update(newSet)
        }
    }


    override fun onExtraInterruptChange(newValue: String, date: String, type: String) {

        val format = String.format("%s_%s", type, date)
        val setDB = settingsDao!!.getSetting(format)
        if (setDB.isEmpty()) {
            settingsDao!!.insertSetting(SettingsEntity(key = format, value = newValue))
        } else {

            val newSet = setDB.first()
            newSet.value = newValue
            settingsDao!!.update(newSet)
        }
    }

    override fun onVacationChange(newValue: String, date: String) {
        val dateDB = dateDao!!.getDate(day, month, year)
        var newSh = 0
        when (newValue) {
            "" -> newSh = 0
            resources.getString(R.string.left_subtitle_7) -> newSh = 1
            resources.getString(R.string.left_subtitle_8) -> newSh = 2
            resources.getString(R.string.left_subtitle_9) -> newSh = 3
        }
        if (dateDB.isEmpty()) {
            dateDao!!.insertDate(
                DateEntity(
                    day = day,
                    month = month,
                    year = year,
                    isPermit = newSh,
                    shift = 0
                )
            )
        } else {
            val newDate = dateDB.first()
            newDate.isPermit = newSh
            dateDao!!.update(newDate)
        }
    }
}