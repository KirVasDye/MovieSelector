package com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.movieselector.autorization.models.User
import com.example.movieselector.mainmenu.apirequests.Controller
import com.example.movieselector.mainmenu.getAPI
import com.example.movieselector.mainmenu.models.MoreMovie
import com.example.movieselector.mainmenu.models.MovieResponse
import com.example.movieselector.mainmenu.models.Page
import com.example.movieselector.mainmenu2.exeptions.ZeroPageEX
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.UserListFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class UserListViewModel : ViewModel() {
    val TAG = "UserListViewModel"
    private val controller = Controller()
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference
    private val extraMovieList: MutableList<MoreMovie> = arrayListOf<MoreMovie>()
    private val uid = Firebase.auth.currentUser?.uid
    /*CorutionScope(Dispatchers.IO).lounch{
        val data = controller.prepareMoviesCall().getTopRatedMovies(getAPI(), 1)?.execute()
            ?.body()?.total_pages
    }*/
    interface MovieListInterface{
        fun data(list: MutableList<MoreMovie>)
    }
    interface UserData{
        fun data(user: User)
    }
    interface PageMax{
        fun data(page: Int)
    }

    //val dataMovie = MovieListInterface
    private var _moreMovieList = MutableLiveData<MutableList<MoreMovie>>().apply {
        getMovieList(object : UserListViewModel.MovieListInterface{
            override fun data(list: MutableList<MoreMovie>) {
                value = list
            }
        })
    }

    private var _userData = MutableLiveData<User>().apply {
        getUserData(object : UserListViewModel.UserData{
            override fun data(user: User) {
                value = user
            }
        })
    }

    private var _pageMax = MutableLiveData<Int>().apply {
        getMaxPage(object : PageMax{
            override fun data(page: Int) {
                value = page
            }
        })
    }

    private fun getMaxPage(page: PageMax){
        controller.prepareMoviesCall().getTopRatedMovies(getAPI())?.enqueue(
            object : retrofit2.Callback<MovieResponse?>{
                override fun onResponse(
                    call: Call<MovieResponse?>,
                    response: Response<MovieResponse?>
                ) {
                    page.data(response.body()?.total_pages ?: 100)
                }

                override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {}

            }
        )
    }

    private fun getUserData(userData: UserData){
        var user: User
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Users").child(uid.toString())
        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java) ?: User()
                Log.d(TAG, "${user.email}")
                userData.data(user)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getMovieList(list: MovieListInterface){
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Pages").child(uid.toString())
        users.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                extraMovieList.clear()
                controller.prepareMoviesCall().getTopRatedMovies(
                    getAPI(),
                    snapshot.getValue(Page::class.java)?.value ?: Page().value
                )?.enqueue(object :
                    retrofit2.Callback<MovieResponse?> {
                    override fun onResponse(
                        call: Call<MovieResponse?>?,
                        response: Response<MovieResponse?>
                    ) {
                        Log.d(TAG, response.body()?.page.toString())
                        for (j in response.body()!!.results) {
                            controller.prepareMoviesCall().getMovieById(j.id, getAPI())
                                ?.enqueue(object :
                                    retrofit2.Callback<MoreMovie?> {
                                    override fun onResponse(
                                        call: Call<MoreMovie?>,
                                        response: Response<MoreMovie?>
                                    ) {
                                        response.body()?.let {
                                            Log.d(TAG, "$it")
                                            extraMovieList.add(it)
                                        }
                                        list.data(extraMovieList)
                                    }

                                    override fun onFailure(call: Call<MoreMovie?>, t: Throwable) {}
                                })
                        }
                    }

                    override fun onFailure(call: Call<MovieResponse?>?, t: Throwable) {}
                })
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    var moreMovieList: LiveData<MutableList<MoreMovie>> = _moreMovieList
    val userData: LiveData<User> = _userData
    var pageMax: LiveData<Int> = _pageMax

    /*fun loadNext(){
        page++
        _moreMovieList.apply {
            getMovieList(object : UserListViewModel.MovieListInterface{
                override fun data(list: MutableList<MoreMovie>) {
                    value = list
                }
            })
        }
        moreMovieList = _moreMovieList
    }*/

    /*fun loadPrevious(){
        if(page-1 == 0){
            throw ZeroPageEX()
        }
        _moreMovieList.apply {
            getMovieList(object : UserListViewModel.MovieListInterface{
                override fun data(list: ArrayList<MoreMovie>) {
                    value = list
                }
            })
        }
        moreMovieList = _moreMovieList
    }*/
}
