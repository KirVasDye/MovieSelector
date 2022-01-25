package com.example.movieselector.testdir

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.movieselector.R
import com.example.movieselector.mainmenu.apirequests.Controller
import com.example.movieselector.mainmenu.getAPI
import com.example.movieselector.mainmenu.models.MoreMovie
import com.example.movieselector.mainmenu.models.Movie
import com.example.movieselector.mainmenu.models.MovieResponse
import com.example.movieselector.toastwindow.eventAlert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestActivity : AppCompatActivity() {
    private lateinit var testText: TextView
    val TAG = "TestActivity"
    lateinit var testV: TestV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)
        testText = findViewById(R.id.info)
        testV = ViewModelProvider(this)[TestV::class.java]
        testV.getMovieList()
        testV.moreMovieList.observe(this, {
            for(i in it){
                Log.d(TAG, "$i")
                testText.text = i.toString()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        /*val moreMovieList = arrayListOf<MoreMovie>()
        for (i in 1 until pages) {
           controller.prepareMoviesCall().getTopRatedMovies(getAPI(),i)?.enqueue(object : Callback<MovieResponse?> {
                override fun onResponse(
                    call: Call<MovieResponse?>?,
                    response: Response<MovieResponse?>
                ) {
                    for(j in response.body()!!.results){
                        controller.prepareMoviesCall().getMovieById(j.id, getAPI())?.enqueue(object : Callback<MoreMovie?>{
                            override fun onResponse(
                                call: Call<MoreMovie?>,
                                response: Response<MoreMovie?>
                            ) {
                                response.body()?.let {
                                    Log.d(TAG, "$it")
                                    moreMovieList.add(it)
                                }
                            }

                            override fun onFailure(call: Call<MoreMovie?>, t: Throwable) {
                                eventAlert(findViewById(R.id.info), "Хуйня давай по новой")
                            }

                        })
                    }
                }

                override fun onFailure(call: Call<MovieResponse?>?, t: Throwable) {
                    eventAlert(findViewById(R.id.info), "Хуйня давай по новой")
                }
            })

        }*/

    }

}