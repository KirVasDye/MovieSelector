package com.example.movieselector.mainmenu.models

object Time {
    fun intToStringTime(timeInt: Int): String{
        return "${timeInt/60}h ${timeInt%60}m"
    }
}