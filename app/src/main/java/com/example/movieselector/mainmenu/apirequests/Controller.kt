package com.example.movieselector.mainmenu.apirequests
import com.example.movieselector.apires.getURL
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit

import com.google.gson.GsonBuilder


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