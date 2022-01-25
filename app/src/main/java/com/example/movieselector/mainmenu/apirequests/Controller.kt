package com.example.movieselector.mainmenu.apirequests
import android.app.Activity
import android.app.Application
import com.example.movieselector.mainmenu.getURL
import retrofit2.converter.gson.GsonConverterFactory
import com.example.movieselector.mainmenu.models.MovieResponse

import retrofit2.Retrofit

import com.google.gson.GsonBuilder
import retrofit2.Call


class Controller{
    private fun prepare(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder().baseUrl(getURL())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun prepareMoviesCall(): EndPointInterface {
        return prepare().create(EndPointInterface::class.java)
    }
}