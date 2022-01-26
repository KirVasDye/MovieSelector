package com.example.movieselector.mainmenu.models

class Duration(
    val hour: Int = 0,
    val minute: Int = 0,
    val error: Int = 0,
) {
    override fun toString(): String {
        return "${hour}h, ${minute}m, ${error}error"
    }
}