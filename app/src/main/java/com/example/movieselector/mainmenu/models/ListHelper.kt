package com.example.movieselector.mainmenu.models

object ListHelper {
    fun listContainsMovie(listMovie: MutableList<MoreMovie>, movie: MoreMovie): Boolean{
        var res = false
        for(film in listMovie){
            if(film.id == movie.id){
                res = true
                break
            }
        }
        return res
    }
    fun listContainsGenres(listGenres: List<Tag>, genres: Tag): Boolean{
        var res = false
        for(tag in listGenres){
            if(tag.id == genres.id){
                res = true
                break
            }
        }
        return res
    }
}