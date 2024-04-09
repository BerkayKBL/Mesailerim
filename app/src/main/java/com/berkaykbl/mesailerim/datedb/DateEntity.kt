package com.berkaykbl.mesailerim.datedb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "dates")
data class DateEntity(
    @PrimaryKey(autoGenerate = true) val uid : Int = 0,
    @ColumnInfo(name = "day")
    val day: Int,
    @ColumnInfo(name = "month")
    val month: Int,
    @ColumnInfo(name = "year")
    val year: Int,
    @ColumnInfo(name = "isPermit")
    var isPermit: Int,
    @ColumnInfo(name = "shift")
    var shift: Int
)


@Entity(
    tableName = "settings",
    indices = [Index(value = ["setting_key"], unique = true)]
)
data class SettingsEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "setting_key")
    val key: String,
    @ColumnInfo(name = "setting_value")
    var value: String,
    @ColumnInfo(name = "setting_extra")
    val extra: String = "",
)