package com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieselector.autorization.models.User
import com.example.movieselector.mainmenu.getAPI
import com.example.movieselector.mainmenu.models.MoreMovie
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WatchListViewModel : ViewModel() {
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference
    private val uid = Firebase.auth.currentUser?.uid
    val extsraMovieList = arrayListOf<MoreMovie>()
    private interface MovieInterface{
        fun data(movie: ArrayList<MoreMovie>)
    }
    private var _moreMovie = MutableLiveData<ArrayList<MoreMovie>>().apply {
        getMovie(object : MovieInterface{
            override fun data(movie: ArrayList<MoreMovie>) {
                value = movie
            }
        })
    }
    private fun getMovie(movie: MovieInterface){
        db = FirebaseDatabase.getInstance()
        users = db.getReference("films_id").child(uid.toString())
        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    extsraMovieList.add(it.getValue(MoreMovie::class.java) ?: MoreMovie())
                }
                extsraMovieList.sortBy { it.title }
                movie.data(extsraMovieList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
    var moreMovieList: LiveData<ArrayList<MoreMovie>> = _moreMovie
}