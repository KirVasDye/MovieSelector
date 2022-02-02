package com.example.movieselector.adminres

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieselector.mainmenu.apirequests.Controller
import com.example.movieselector.apires.getAPI
import com.example.movieselector.mainmenu.models.MoreMovie
import com.example.movieselector.mainmenu.models.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class TestV : ViewModel() {
    val TAG = "TestV"
    private val controller = Controller()
    val extsraMovieList = arrayListOf<MoreMovie>()
    /*CorutionScope(Dispatchers.IO).lounch{
        val data = controller.prepareMoviesCall().getTopRatedMovies(getAPI(), 1)?.execute()
            ?.body()?.total_pages
    }*/
    private var _moreMovieList = MutableLiveData<ArrayList<MoreMovie>>().apply { value = extsraMovieList }
    fun getMovieList(){
        CoroutineScope(Dispatchers.IO).launch{
            controller.prepareMoviesCall().getTopRatedMovies(getAPI(),1)?.enqueue(object :
                retrofit2.Callback<MovieResponse?> {
                override fun onResponse(
                    call: Call<MovieResponse?>?,
                    response: Response<MovieResponse?>
                ) {
                    for(j in response.body()!!.results){
                        controller.prepareMoviesCall().getMovieById(j.id, getAPI())?.enqueue(object :
                            retrofit2.Callback<MoreMovie?> {
                            override fun onResponse(
                                call: Call<MoreMovie?>,
                                response: Response<MoreMovie?>
                            ) {
                                response.body()?.let {
                                    Log.d(TAG, "$it")
                                    extsraMovieList.add(it)
                                }
                            }

                            override fun onFailure(call: Call<MoreMovie?>, t: Throwable) {}

                        })
                    }
                }
                override fun onFailure(call: Call<MovieResponse?>?, t: Throwable) {
                }
            })
        }
    }
    val moreMovieList: LiveData<ArrayList<MoreMovie>> = _moreMovieList
}

