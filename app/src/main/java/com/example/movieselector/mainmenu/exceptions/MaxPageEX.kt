package com.example.movieselector.mainmenu.exceptions

class MaxPageEX : Exception() {
    override val message: String?
        get() = "Page equal maximum"
}