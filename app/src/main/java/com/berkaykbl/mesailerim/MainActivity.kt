package com.berkaykbl.mesailerim

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.GridView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.berkaykbl.mesailerim.Adapters.DateAdapter
import com.berkaykbl.mesailerim.Adapters.DateItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity() {

    private var gridView: GridView? = null
    private var arrayList: ArrayList<DateItem>? = null
    private var dateAdapter: DateAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gridView = findViewById(R.id.calendar_grid)
        arrayList = ArrayList()
        arrayList = setDataList()
        dateAdapter = DateAdapter(applicationContext, arrayList!!)
        gridView?.adapter = dateAdapter

    }

    private fun setDataList(): ArrayList<DateItem> {
        var arrayList: ArrayList<DateItem> = ArrayList()

        val dateFormat: DateFormat = SimpleDateFormat("MM")
        val date = Date()
        Log.d("Month", dateFormat.format(date))
        return arrayList
    }
}