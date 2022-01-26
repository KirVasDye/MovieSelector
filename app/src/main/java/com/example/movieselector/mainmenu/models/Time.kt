package com.example.movieselector.mainmenu.models

object Time {
    fun intToStringTime(timeInt: Int) = "${timeInt/60}h ${timeInt%60}m"
    fun durationToInt(duration: Duration) = duration.hour * 60 + duration.minute
}