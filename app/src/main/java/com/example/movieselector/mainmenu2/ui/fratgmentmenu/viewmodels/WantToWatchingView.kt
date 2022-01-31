package com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieselector.mainmenu.models.MoreMovie
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class WantToWatchingView : ViewModel() {
    private val uid = Firebase.auth.currentUser?.uid
    private lateinit var db: FirebaseDatabase
    private lateinit var interestedFilm: DatabaseReference

    private interface InterestingFilmInterface{
        fun data(movie: MoreMovie)
    }

    private var _interestingFilm = MutableLiveData<MoreMovie>().apply {
        getInterestingFilm(object : InterestingFilmInterface{
            override fun data(movie: MoreMovie) {
                value = movie
            }
        })
    }

    private fun getInterestingFilm(movie: InterestingFilmInterface){
        db = FirebaseDatabase.getInstance()
        interestedFilm = db.getReference("InterestingFilm")
            .child(uid.toString())
        interestedFilm.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                movie.data(
                    snapshot.getValue(MoreMovie::class.java) ?: MoreMovie()
                )
            }
            override fun onCancelled(error: DatabaseError) {}

        })
    }

    var interestingFilm = _interestingFilm
}