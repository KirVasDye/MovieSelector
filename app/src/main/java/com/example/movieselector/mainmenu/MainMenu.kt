package com.example.movieselector.mainmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movieselector.R

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)
    }
    /*class MainMenuActivity : Activity() {
    private lateinit var email: TextView
    private lateinit var uid: TextView
    private lateinit var name: TextView

    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference

    private lateinit var movieRecyclerView: RecyclerView
    val TAG: String = MainMenuActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        val controller = Controller()
        movieRecyclerView = findViewById(R.id.films_list)
        movieRecyclerView.layoutManager = LinearLayoutManager(this)

        val arguments = intent.extras
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Users").child(arguments!!.get("uid").toString())
        email = findViewById(R.id.adress)
        uid = findViewById(R.id.uid)
        name = findViewById(R.id.name)
        uid.text = arguments!!.get("uid").toString()
        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                email.text = snapshot.child("email").value.toString()
                name.text = snapshot.child("name").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                eventAlert(findViewById(R.id.uid), "${error.message}")
            }
        })

        controller.prepareMoviesCall().getTopRatedMovies(getAPI())?.enqueue(object : Callback<MovieResponse?> {
            override fun onResponse(
                call: Call<MovieResponse?>?,
                response: Response<MovieResponse?>
            ) {
                val movieList: List<Movie> = response.body()!!.results
                movieRecyclerView.adapter = MoviesAdapter(movieList)
            }

            override fun onFailure(call: Call<MovieResponse?>?, t: Throwable) {
                Log.d(TAG, t.localizedMessage)
            }
        })


    }
}*/
}