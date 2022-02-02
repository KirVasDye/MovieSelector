package com.example.movieselector.mainmenu.exceptions

class ZeroPageEX : Exception(){
    override val message: String?
        get() = "Zero page"
}