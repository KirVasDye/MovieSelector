package com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieselector.mainmenu.apirequests.Controller
import com.example.movieselector.mainmenu.getAPI
import com.example.movieselector.mainmenu.models.GenresResponse
import com.example.movieselector.mainmenu.models.Tag
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenresChooseViewModel : ViewModel() {
    val TAG = "GenresChooseViewModel"
    private val uid = Firebase.auth.currentUser?.uid
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference
    private val listGenres: MutableList<Tag> = arrayListOf<Tag>()
    private val controller = Controller()
    private interface GenresInterface{
        fun data(tag: Tag)
    }
    private interface GenresListInterface{
        fun data(list: MutableList<Tag>)
    }
    private var _listTags = MutableLiveData<MutableList<Tag>>().apply {
        getListTags(object : GenresListInterface{
            override fun data(list: MutableList<Tag>) {
                value = list
            }

        })
    }
    private var _tagStatus = MutableLiveData<Tag>().apply {
        getTagChoose(object : GenresInterface{
            override fun data(tag: Tag) {
                value = tag
            }
        })
    }
    private fun getTagChoose(tag: GenresInterface){
        db = FirebaseDatabase.getInstance()
        users = db.getReference("GenresStatus").child(uid.toString())
        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tag.data(
                    snapshot.getValue(Tag::class.java) ?: Tag()
                )
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
    private fun getListTags(list: GenresListInterface){
        CoroutineScope(Dispatchers.IO).launch {
            listGenres.clear()
            controller.prepareMoviesCall().getGenresList(getAPI())?.enqueue(object : Callback<GenresResponse?>{
                override fun onResponse(call: Call<GenresResponse?>, response: Response<GenresResponse?>) {
                    response.body()?.genres?.forEach {
                        listGenres.add(it)
                    }
                    listGenres.add(Tag())
                    listGenres.sortBy { it.name }
                    list.data(listGenres)
                }

                override fun onFailure(call: Call<GenresResponse?>, t: Throwable) {}

            })
        }
    }
    var tagStatus = _tagStatus
    var listTags = _listTags
}