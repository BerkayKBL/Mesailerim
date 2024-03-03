package com.berkaykbl.mesailerim.Adapters

class DateItem {

    var day: String = ""
    var isSunday: Boolean = false
    var isToday: Boolean = false
    var isPermit: Boolean = false
    var shift: Double = 0.0

    constructor(
        day: String,
        isSunday: Boolean,
        isToday: Boolean,
        isPermit: Boolean,
        shift: Double
    ) {
        this.day = day
        this.isSunday = isSunday
        this.isToday = isToday
        this.isPermit = isPermit
        this.shift = shift
    }
}