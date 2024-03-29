package com.example.movieselector.mainmenu.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieselector.mainmenu.models.Duration
import com.example.movieselector.mainmenu.models.Tag
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class SelectionViewModel : ViewModel() {
    val TAG = "SelectionViewModel"
    private val uid = Firebase.auth.currentUser?.uid
    private lateinit var db: FirebaseDatabase
    lateinit var users: DatabaseReference
    lateinit var refTagChoose: ValueEventListener
    lateinit var refDurationChoose: ValueEventListener
    lateinit var durationStatus: DatabaseReference
    private interface GenresInterface{
        fun data(tag: Tag)
    }
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
    private var _tagStatus = MutableLiveData<Tag>().apply {
        getTagChoose(object : GenresInterface {
            override fun data(tag: Tag) {
                value = tag
            }
        })
    }
    private fun getTagChoose(tag: GenresInterface){
        db = FirebaseDatabase.getInstance()
        users = db.getReference("GenresStatus").child(uid.toString())
        refTagChoose = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tag.data(
                    snapshot.getValue(Tag::class.java) ?: Tag()
                )
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        users.addValueEventListener(refTagChoose)
    }
    private fun getDurationChoose(duration: DurationChooseInterface){
        db = FirebaseDatabase.getInstance()
        durationStatus = db.getReference("DurationChoose").child(uid.toString())
        refDurationChoose = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                duration.data(
                    snapshot.getValue(Duration::class.java) ?: Duration()
                )
            }

            override fun onCancelled(error: DatabaseError) {}

        }
        durationStatus.addValueEventListener(refDurationChoose)
    }
    var tagStatus = _tagStatus
    var durationChoose = _durationChoose
}