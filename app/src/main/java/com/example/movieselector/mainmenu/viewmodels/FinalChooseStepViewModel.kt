package com.example.movieselector.mainmenu.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieselector.mainmenu.models.MoreMovie
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class FinalChooseStepViewModel : ViewModel() {
    private val TAG = "FinalChooseStepView"
    private val uid = Firebase.auth.currentUser?.uid
    private lateinit var db: FirebaseDatabase
    private lateinit var interestedFilm: DatabaseReference

    private interface InterestingFilmInterface{
        fun data(movie: MoreMovie)
    }

    private var _interestingFilm = MutableLiveData<MoreMovie>().apply {
        getInterestingFilm(object : InterestingFilmInterface {
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

    /*fun setInCalendar(dateAndTime: Calendar, cv: ContentValues, cr: ContentResolver){
        /*val startEvent: Long = dateAndTime.timeInMillis
        Log.d(TAG, "$startEvent in Long ")
        Log.d(TAG, "${dateAndTime.get(Calendar.YEAR)} year in Long " +
                "${dateAndTime.get(Calendar.MONTH)} month in Long " +
                "${dateAndTime.get(Calendar.DAY_OF_MONTH)} day in Long ")
        Log.d(TAG, "${dateAndTime.get(Calendar.HOUR_OF_DAY)} hour in Long " +
                "${dateAndTime.get(Calendar.MINUTE)} minute in Long\n")
        cv.put(CalendarContract.Events.TITLE, "Watching film")
        cv.put(CalendarContract.Events.DESCRIPTION, "You want watching ${interestingFilm.title}")
        cv.put(CalendarContract.Events.EVENT_LOCATION, "In your phone")
        cv.put(CalendarContract.Events.DTSTART, startEvent)
        cv.put(CalendarContract.Events.CALENDAR_ID, 3)
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, dateAndTime.timeZone.id)
        val uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv)*/
    }*/

}