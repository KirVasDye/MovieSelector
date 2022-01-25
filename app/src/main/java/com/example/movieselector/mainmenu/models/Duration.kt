package com.example.movieselector.mainmenu.models

class Duration(
    val hour: Short = 0,
    val minute: Short = 0,
    val error: Short = 0,
) {
    override fun toString(): String {
        return "${hour}h, ${minute}m, ${error}error"
    }
}