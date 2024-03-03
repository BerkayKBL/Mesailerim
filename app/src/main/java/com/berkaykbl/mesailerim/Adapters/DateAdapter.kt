package com.berkaykbl.mesailerim.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.graphics.Color
import com.berkaykbl.mesailerim.R
import org.w3c.dom.Text

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

        var day: TextView = view.findViewById<TextView>(R.id.day)
        var shift: TextView = view.findViewById(R.id.shift)
        var dateItem : DateItem = arrayList[p0]

        day.text = dateItem.day

        if (dateItem.isSunday) {
            day.setTextColor(R.color.teal_200)
        }

        if (dateItem.isToday) {
            day.setTextColor(R.color.weekcolor)
        }

        if (dateItem.isPermit) {
            day.setTextColor(R.color.red)
            shift.text = "İzinli"
        } else if (dateItem.shift > 0.0) {
            shift.text = "Mesai: "+dateItem.shift.toString()
        } else {
            shift.text = " "
        }

        return view
    }

}