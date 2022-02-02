package com.example.movieselector.mainmenu.models

class UserIdFilms(
    var idUser: String = "",
    var listFilms: List<String> = listOf()
){
    override fun toString() = "$idUser: $listFilms"
}