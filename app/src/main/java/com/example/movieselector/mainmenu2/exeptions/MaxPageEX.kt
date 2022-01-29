package com.example.movieselector.mainmenu2.exeptions

class MaxPageEX : Exception() {
    override val message: String?
        get() = "Page equal maximum"
}