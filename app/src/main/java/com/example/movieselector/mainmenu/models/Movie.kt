package com.example.movieselector.mainmenu.models

open class Movie (
    val id: Int,
    val original_title: String,
    val overview: String,
    val vote_average: Double,
){
    override fun toString() = String.format("Id: $id, Title: $original_title, Rate: $vote_average")
}