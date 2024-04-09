package com.berkaykbl.mesailerim.datedb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDao {

    @Insert
    fun insertDate(date: DateEntity)

    @Query("SELECT * FROM dates")
    fun getAllDates(): List<DateEntity>

    @Query("SELECT * FROM dates WHERE month = :month AND year = :year")
    fun getDateWithMonthWithListener(month: Int, year: Int): Flow<List<DateEntity>>

    @Query("SELECT * FROM dates WHERE month = :month AND year = :year")
    fun getDateWithMonth(month: Int, year: Int): List<DateEntity>

    @Query("SELECT * FROM dates WHERE day = :day AND month = :month AND year = :year")
    fun getDate(day: Int, month: Int, year: Int): List<DateEntity>

    @Query("DELETE FROM dates WHERE month = :month AND year = :year")
    fun deleteMontlyData(month: Int, year: Int)

    @Update
    fun update(dateEntity: DateEntity)

    /*@Query("SELECT * FROM dates WHERE date IN (:month)")
    fun getMonthDate()*/

}


@Dao
interface SettingsDao {

    @Insert
    fun insertSetting(setting: SettingsEntity)

    @Query("SELECT * FROM settings WHERE setting_key = :key")
    fun getSettingWithListener(key: String): Flow<List<SettingsEntity>>

    @Query("SELECT * FROM settings WHERE setting_key = :key")
    fun getSetting(key: String): List<SettingsEntity>

    @Update
    fun update(settingsEntity: SettingsEntity)

    /*@Query("SELECT * FROM dates WHERE date IN (:month)")
    fun getMonthDate()*/

}