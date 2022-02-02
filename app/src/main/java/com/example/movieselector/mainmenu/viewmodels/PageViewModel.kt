package com.example.movieselector.mainmenu.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieselector.mainmenu.models.Page
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class PageViewModel() : ViewModel() {
    val TAG = "PageViewModelViewModel"
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val uid = Firebase.auth.currentUser?.uid
    val users: DatabaseReference = db.getReference("Pages").child(uid.toString())
    lateinit var refPages: ValueEventListener
    interface PageInfo{
        fun data(page: Page)
    }
    private var _pageInfo = MutableLiveData<Page>().apply {
        getUserPage(object : PageInfo {
            override fun data(page: Page) {
                value = page
            }
        })
    }

    private fun getUserPage(pageInterface: PageInfo){
        var page: Page
        refPages = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                page = snapshot.getValue(Page::class.java) ?: Page()
                Log.d(TAG, page.value.toString())
                pageInterface.data(page)
            }

            override fun onCancelled(error: DatabaseError) {}

        }
        users.addValueEventListener(refPages)
    }

    var pageInfo: LiveData<Page> = _pageInfo

}
