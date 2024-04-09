package com.berkaykbl.mesailerim.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.berkaykbl.mesailerim.Adapters.DateItem
import com.berkaykbl.mesailerim.MainActivity
import com.berkaykbl.mesailerim.R
import com.berkaykbl.mesailerim.Test
import java.util.Calendar

class DateAdapter(var context: Context, var arrayList: ArrayList<DateItem>) : BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(p0: Int): Any {
        return arrayList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ResourceAsColor")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view: View = View.inflate(context, R.layout.grid_item, null)

        var day: TextView = view.findViewById(R.id.day)
        var day_bg: LinearLayout = view.findViewById(R.id.day_bg)
        var shift: TextView = view.findViewById(R.id.shift)
        var dateItem : DateItem = arrayList[p0]

        day.text = dateItem.day


        if (dateItem.isSunday) {
            day.setTextColor(ContextCompat.getColor(context, R.color.sundayColor))
        }

        if (dateItem.isToday) {
            view.isHovered = true
        }

        if (dateItem.isPermit != 0) {
            day.setTextColor(ContextCompat.getColor(context, R.color.red))
            shift.text = "İzinli"
        } else if (dateItem.shift > 0.0) {
            shift.text = "Mesai: "+dateItem.shift.toString()
        } else {
            shift.text = " "
        }

        if (dateItem.isSelected) {
            view.isSelected = true
        }




        return view
    }

}