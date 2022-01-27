package com.example.movieselector.mainmenu.models

import com.example.movieselector.mainmenu.apirequests.Controller
import com.example.movieselector.mainmenu.getAPI

class MoreMovie(
    id: Int = 0,
    original_title: String = "",
    overview: String = "",
    vote_average: Double = 0.0,
    val runtime: Int = 0,
    val poster_path: String = "",
    val title: String = "",
    val genres: List<Tag> = arrayListOf()
) : Movie(
    id,
    original_title,
    overview,
    vote_average,
) {
    override fun toString(): String {
        return super.toString() + ", runtime: ${Time.intToStringTime(runtime)} poster_path: ${Image.getImageURL(poster_path)}, genres: $genres\n"
    }
    fun stringGenres(): String{
        var res = ""
        genres.forEach{
            res += "${it.name}\n"
        }
        return res
    }
}