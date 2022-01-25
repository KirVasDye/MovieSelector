package com.example.movieselector.mainmenu.models

class Tag(
    var id: Int = 0,
    var name: String = "",
){
    override fun toString(): String {
        return "$name"
    }
}