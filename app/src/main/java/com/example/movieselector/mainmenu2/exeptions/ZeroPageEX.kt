package com.example.movieselector.mainmenu2.exeptions

class ZeroPageEX : Exception(){
    override val message: String?
        get() = "Zero page"
}