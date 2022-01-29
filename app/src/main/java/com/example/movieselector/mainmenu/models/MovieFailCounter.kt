package com.example.movieselector.mainmenu.models

class MovieFailCounter(
    var failureCounter: Int = 0,
    var filmId: Int = 0,
){
    override fun toString(): String = "$filmId : $failureCounter"
}