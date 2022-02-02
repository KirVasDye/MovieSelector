package com.example.movieselector.mainmenu.models

import android.net.Uri
import com.example.movieselector.apires.getURLImage

object Image {
    fun getImageURL(posterPath: String, size: Int = 45): Uri{
        return Uri.parse("${getURLImage(size)}$posterPath")
    }
}