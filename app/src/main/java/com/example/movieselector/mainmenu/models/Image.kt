package com.example.movieselector.mainmenu.models

import android.net.Uri
import com.example.movieselector.mainmenu.getURLImage

object Image {
    fun getImageURL(posterPath: String, size: Int = 45): Uri{
        return Uri.parse("${getURLImage(size)}$posterPath")
    }
}