package com.example.movieselector.mainmenu.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieselector.mainmenu.models.Duration
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class DurationChooseViewModel : ViewModel() {
    private val uid = Firebase.auth.currentUser?.uid
    private lateinit var db: FirebaseDatabase
    private lateinit var durationStatus: DatabaseReference

    private interface DurationChooseInterface{
        fun data(duration: Duration)
    }
    private var _durationChoose = MutableLiveData<Duration>().apply {
        getDurationChoose(object : DurationChooseInterface {
            override fun data(duration: Duration) {
                value = duration
            }
        })
    }
    private fun getDurationChoose(duration: DurationChooseInterface){
        db = FirebaseDatabase.getInstance()
        durationStatus = db.getReference("DurationChoose").child(uid.toString())
        durationStatus.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                duration.data(
                    snapshot.getValue(Duration::class.java) ?: Duration()
                )
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
    var durationChoose = _durationChoose

}