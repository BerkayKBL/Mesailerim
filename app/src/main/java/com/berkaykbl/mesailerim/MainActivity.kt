package com.berkaykbl.mesailerim

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val day = LocalDate.now().dayOfMonth
        val month = LocalDate.now().month
        val year = LocalDate.now().year
        val day1 = LocalDate.of(year, month, 1)
        println(month)
        println(day)
        println(day1)
    }
}