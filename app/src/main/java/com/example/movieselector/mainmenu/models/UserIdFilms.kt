package com.example.movieselector.mainmenu.models

import android.provider.ContactsContract
import com.example.movieselector.autorization.models.User

class UserIdFilms(
    var idUser: String = "",
    var listFilms: List<String> = listOf()
){
    override fun toString() = "$idUser: $listFilms"
}