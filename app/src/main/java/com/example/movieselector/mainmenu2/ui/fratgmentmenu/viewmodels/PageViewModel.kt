package com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieselector.mainmenu.apirequests.Controller
import com.example.movieselector.mainmenu.models.Page
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class PageViewModel() : ViewModel() {
    val TAG = "PageViewModelViewModel"
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val uid = Firebase.auth.currentUser?.uid
    private val users: DatabaseReference = db.getReference("Pages").child(uid.toString())
    interface PageInfo{
        fun data(page: Page)
    }
    private var _pageInfo = MutableLiveData<Page>().apply {
        getUserPage(object : PageViewModel.PageInfo{
            override fun data(page: Page) {
                value = page
            }
        })
    }

    private fun getUserPage(pageInterface: PageInfo){
        var page: Page
        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                page = snapshot.getValue(Page::class.java) ?: Page()
                Log.d(TAG, page.value.toString())
                pageInterface.data(page)
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    var pageInfo: LiveData<Page> = _pageInfo

}
