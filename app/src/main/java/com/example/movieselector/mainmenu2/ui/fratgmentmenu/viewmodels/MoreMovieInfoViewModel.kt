package com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels

import android.app.Application
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieselector.mainmenu.apirequests.Controller
import com.example.movieselector.mainmenu.getAPI
import com.example.movieselector.mainmenu.models.MoreMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoreMovieInfoViewModel(application: Application) : AndroidViewModel(application) {
    private var settings: SharedPreferences = application.applicationContext.getSharedPreferences("id", Context.MODE_PRIVATE)
    private var id = settings.getString("id", "497")
    private val controller = Controller()
    private interface MovieInterface{
        fun data(movie: MoreMovie)
    }
    private var _moreMovie = MutableLiveData<MoreMovie>().apply {
        getMovie(object : MoreMovieInfoViewModel.MovieInterface{
            override fun data(movie: MoreMovie) {
                value = movie
            }
        })
    }
    private fun getMovie(movie: MovieInterface){
        CoroutineScope(Dispatchers.IO).launch{
            controller.prepareMoviesCall().getMovieById(id!!.toInt(), getAPI())?.enqueue(object :
                Callback<MoreMovie?>{
                override fun onResponse(call: Call<MoreMovie?>, response: Response<MoreMovie?>) {
                    response.body()?.let{
                        movie.data(it)
                    }
                }
                override fun onFailure(call: Call<MoreMovie?>, t: Throwable) {}

            })
        }
    }
    var moreMovie = _moreMovie
}