package com.example.movieselector.mainmenu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieselector.mainmenu.models.MoreMovie
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class WatchListViewModel : ViewModel() {
    private lateinit var db: FirebaseDatabase
    lateinit var users: DatabaseReference
    lateinit var refMovie: ValueEventListener
    private val uid = Firebase.auth.currentUser?.uid
    val extsraMovieList = arrayListOf<MoreMovie>()
    private interface MovieInterface{
        fun data(movie: ArrayList<MoreMovie>)
    }
    private var _moreMovie = MutableLiveData<ArrayList<MoreMovie>>().apply {
        getMovie(object : MovieInterface {
            override fun data(movie: ArrayList<MoreMovie>) {
                value = movie
            }
        })
    }
    private fun getMovie(movie: MovieInterface){
        db = FirebaseDatabase.getInstance()
        users = db.getReference("films_id").child(uid.toString())
        refMovie = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    extsraMovieList.add(it.getValue(MoreMovie::class.java) ?: MoreMovie())
                }
                extsraMovieList.sortBy { it.title }
                movie.data(extsraMovieList)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        users.addValueEventListener(refMovie)
    }
    var moreMovieList: LiveData<ArrayList<MoreMovie>> = _moreMovie
}