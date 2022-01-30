package com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieselector.mainmenu.apirequests.Controller
import com.example.movieselector.mainmenu.getAPI
import com.example.movieselector.mainmenu.models.*
import com.example.movieselector.mainmenu2.exeptions.MaxPageEX
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieSelectionViewModel : ViewModel() {
    val TAG = "MovieSelectionViewModel"
    private var page = 1
    private val uid = Firebase.auth.currentUser?.uid
    private val controller = Controller()
    private lateinit var db: FirebaseDatabase
    private lateinit var durationStatus: DatabaseReference
    private lateinit var genresStatus: DatabaseReference
    private lateinit var watchingFilms: DatabaseReference
    private lateinit var interestedFilm: DatabaseReference
    private lateinit var notInterestedFilmsList: DatabaseReference
    private lateinit var notInterestedResList: DatabaseReference
    private var extraWatchingMovieList: ArrayList<MoreMovie> = arrayListOf()
    private var extraMovieList: MutableList<MoreMovie> = arrayListOf()
    private var extraNotInterestedResList: MutableList<MovieFailCounter> = arrayListOf()
    private var extraMovieListHelper: MutableList<MoreMovie> = arrayListOf()

    private interface DurationChooseInterface{
        fun data(duration: Duration)
    }
    private interface GenresStatus{
        fun data(tag: Tag)
    }
    private interface PageMax{
        fun data(page: Int)
    }
    private interface WatchingMovieList{
        fun data(list: ArrayList<MoreMovie>)
    }
    private interface MovieListInterface{
        fun data(list: MutableList<MoreMovie>)
        //fun data(moviePage: MovieResponse)
    }
    private interface NotInterestedRes{
        fun data(noInteres: MutableList<MovieFailCounter>)
    }
    private interface InterestingFilmInterface{
        fun data(movie: MoreMovie)
    }
    private var durationChoose: Duration = Duration()
    init {
        getDurationChoose(object : MovieSelectionViewModel.DurationChooseInterface{
            override fun data(duration: Duration) {
                durationChoose = duration
                Log.d(TAG, "${durationChoose.toString()}")
            }
        })
    }
    private var genresChoose: Tag = Tag()
    init {
        getGenresStatus(object : MovieSelectionViewModel.GenresStatus{
            override fun data(tag: Tag) {
                genresChoose = tag
                Log.d(TAG, "${genresChoose.toString()}")
            }
        })
    }
    private var pageMax: Int = 0
    init {
        getMaxPage(object : PageMax{
            override fun data(page: Int) {
                pageMax = page
                Log.d(TAG, pageMax.toString())
            }
        })
    }
    private var watchingMovieList: MutableList<MoreMovie> = arrayListOf()
    init {
        Log.d(TAG, "Сюдой")
        getWatchingMovieList(object : WatchingMovieList{
            override fun data(list: ArrayList<MoreMovie>) {
                watchingMovieList = list
                Log.d(TAG, watchingMovieList.toString())
            }
        })
    }
    private var notInterestedRes: MutableList<MovieFailCounter> = arrayListOf()
    init {
        getNotInterestedRes(object : NotInterestedRes{
            override fun data(noInteres: MutableList<MovieFailCounter>) {
                notInterestedRes = noInteres
                Log.d(TAG, notInterestedRes.toString())
            }
        })
    }
    private var interestingFilm: MoreMovie = MoreMovie()
    init {
        getPreviousInterestingFilm(object : InterestingFilmInterface{
            override fun data(movie: MoreMovie) {
                interestingFilm = movie
                Log.d(TAG, interestingFilm.toString())
            }
        })
    }

    init {
        Log.d(TAG, page.toString())
    }
    private var _movieList = MutableLiveData<MutableList<MoreMovie>>().apply {
        extraMovieListHelper = arrayListOf()
        getMoreMovieList(object : MovieListInterface{
            override fun data(movie: MutableList<MoreMovie>) {
                if(movie.size == 20){
                    movie.forEach{
                        if(separateMovieList(it)){
                            extraMovieListHelper.add(it)
                        }
                    }
                    value = extraMovieListHelper
                }
                /*if(separateMovieList(movie)){
                    Log.d(TAG, movie.toString())
                    extraMovieList.add(movie)
                    value = extraMovieList
                }*/
            }
        })
    }

    private fun getPreviousInterestingFilm(movie: InterestingFilmInterface){
        db = FirebaseDatabase.getInstance()
        interestedFilm = db.getReference("InterestingFilm")
            .child(uid.toString())
        interestedFilm.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                movie.data(
                    snapshot.getValue(MoreMovie::class.java) ?: MoreMovie()
                )
            }
            override fun onCancelled(error: DatabaseError) {}

        })
    }
    private fun getDurationChoose(duration: MovieSelectionViewModel.DurationChooseInterface){
        db = FirebaseDatabase.getInstance()
        durationStatus = db.getReference("DurationChoose").child(uid.toString())
        durationStatus.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                duration.data(
                    snapshot.getValue(Duration::class.java) ?: Duration()
                )
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
    private fun getGenresStatus(genres: MovieSelectionViewModel.GenresStatus){
        db = FirebaseDatabase.getInstance()
        genresStatus = db.getReference("GenresStatus").child(uid.toString())
        genresStatus.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                genres.data(
                    snapshot.getValue(Tag::class.java) ?: Tag()
                )
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
    private fun getMaxPage(page: PageMax){
        controller.prepareMoviesCall().getTopRatedMovies(
            getAPI()
        )?.enqueue(object : Callback<MovieResponse?>{
            override fun onResponse(
                call: Call<MovieResponse?>,
                response: Response<MovieResponse?>
            ) {
                page.data(response.body()?.total_pages ?: 100)
            }
            override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {}
        })
    }
    private fun getWatchingMovieList(list: WatchingMovieList){
        db = FirebaseDatabase.getInstance()
        watchingFilms = db.getReference("films_id").child(uid.toString())
        watchingFilms.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    extraWatchingMovieList.add(it.getValue(MoreMovie::class.java) ?: MoreMovie())
                }
                list.data(extraWatchingMovieList)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
    private fun getNotInterestedRes(noInteres: NotInterestedRes){
        db = FirebaseDatabase.getInstance()
        notInterestedResList = db.getReference("NotInterestedFilmsCounter").child(uid.toString())
        notInterestedResList.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    extraNotInterestedResList.add(it.getValue(MovieFailCounter::class.java) ?: MovieFailCounter())
                }
                noInteres.data(extraNotInterestedResList)
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
    private fun getMoreMovieList(list: MovieListInterface) {
        //if(page < pageMax) {
        CoroutineScope(Dispatchers.IO).launch {
            controller.prepareMoviesCall().getTopRatedMovies(
                getAPI(),
                page
            )?.enqueue(object : Callback<MovieResponse?> {
                override fun onResponse(
                    call: Call<MovieResponse?>?,
                    response: Response<MovieResponse?>
                ) {
                    extraMovieList.clear()
                    for (movie in response.body()!!.results) {
                        controller.prepareMoviesCall().getMovieById(
                            movie.id,
                            getAPI()
                        )?.enqueue(object : Callback<MoreMovie?> {
                            override fun onResponse(
                                call: Call<MoreMovie?>,
                                response: Response<MoreMovie?>
                            ) {
                                response.body()?.let {
                                    extraMovieList.add(it)
                                }
                                list.data(extraMovieList)
                                /*var film = response.body()!!
                                if((!ListHelper.listContainsMovie(watchingMovieList, film))
                                    &&(ListHelper.listContainsGenres(film.genres, genresChoose))
                                    &&(film.runtime >= Time.durationToInt(durationChoose) - durationChoose.error)
                                    &&(film.runtime <= Time.durationToInt(durationChoose) + durationChoose.error)){

                                    extraMovieList.add(film)

                                }
                                list.data(extraMovieList)*/
                            }

                            override fun onFailure(call: Call<MoreMovie?>, t: Throwable) {}
                        })
                    }
                    //Log.d(TAG, watchingMovieList.toString() + "watchingMovieList")
                }

                override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {}
            })
        }
       // } else {
           // throw MaxPageEX()
       // }
    }

    var movieList: LiveData<MutableList<MoreMovie>> = _movieList

    fun separateMovieList(movie: MoreMovie): Boolean{
        return ((!ListHelper.listContainsMovie(watchingMovieList, movie))
                &&(ListHelper.listContainsGenres(movie.genres, genresChoose))
                &&(movie.runtime >= Time.durationToInt(durationChoose) - durationChoose.error)
                &&(movie.runtime <= Time.durationToInt(durationChoose) + durationChoose.error))
    }

    fun next(){
        page++
        Log.d(TAG, "$page page")
        _movieList.apply {
            extraMovieListHelper = arrayListOf()
            getMoreMovieList(object : MovieListInterface {
                override fun data(movie: MutableList<MoreMovie>) {
                    if (movie.size == 20) {
                        movie.forEach {
                            if (separateMovieList(it)) {
                                extraMovieListHelper.add(it)
                            }
                        }
                        value = extraMovieListHelper
                    }
                }
            })
        }
    }

    fun dropMovie(){
        db = FirebaseDatabase.getInstance()
        notInterestedFilmsList = db.getReference("NotInterestedFilmsCounter")
            .child(uid.toString())
            .child(_movieList.value?.get(0)?.id.toString())
        var idFilm = _movieList.value?.get(0)?.id
        Log.d(TAG, idFilm.toString())
        notInterestedFilmsList.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val movieFailCounter = snapshot.getValue(MovieFailCounter::class.java) ?: MovieFailCounter()
                movieFailCounter!!.failureCounter++
                movieFailCounter.filmId = idFilm!!
                notInterestedFilmsList.setValue(movieFailCounter)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        Log.d(TAG, "$extraMovieListHelper extraMovieListHelper")
        extraMovieListHelper.removeFirst()
        Log.d(TAG, "$extraMovieListHelper extraMovieListHelper")
        _movieList.value = extraMovieListHelper
    }

    fun addToInteresting(){
        val temp = _movieList.value?.first()
        db = FirebaseDatabase.getInstance()
        watchingFilms = db.getReference("films_id")
            .child(uid.toString())
        interestedFilm = db.getReference("InterestingFilm")
            .child(uid.toString())
        if(interestingFilm.id != 0){
            watchingFilms.child(interestingFilm.id.toString())
                .setValue(interestingFilm)
        }
        interestedFilm.setValue(temp)
    }
}