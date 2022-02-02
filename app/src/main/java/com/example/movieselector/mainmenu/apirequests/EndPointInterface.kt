package com.example.movieselector.mainmenu.apirequests

import com.example.movieselector.mainmenu.models.GenresResponse
import com.example.movieselector.mainmenu.models.MoreMovie
import retrofit2.Call
import com.example.movieselector.mainmenu.models.MovieResponse

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface EndPointInterface {
    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") key: String?, @Query("page") page: Int = 1): Call<MovieResponse?>?

    @GET("movie/{id}" )
    fun getMovieById(@Path("id") id: Int = 497,
                     @Query("api_key") key: String?,
                     @Query("language") language: String? = "ru-RU"
    ): Call<MoreMovie?>?

    @GET("genre/movie/list")
    fun getGenresList(@Query("api_key") key: String?,
                      @Query("language") language: String? = "ru-RU"
    ) : Call<GenresResponse?>?
}