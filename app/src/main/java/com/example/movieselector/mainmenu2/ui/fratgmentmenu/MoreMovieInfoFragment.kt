package com.example.movieselector.mainmenu2.ui.fratgmentmenu

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.movieselector.R
import com.example.movieselector.databinding.FragmentMoreMovieInfoBinding
import com.example.movieselector.mainmenu.models.Image
import com.example.movieselector.mainmenu.models.Time
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels.MoreMovieInfoViewModel
import com.example.movieselector.toastwindow.eventAlert
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MoreMovieInfoFragment : Fragment() {

    val TAG = "MoreMovieInfoFragment"

    private lateinit var moreMovieInfoViewModel: MoreMovieInfoViewModel
    private var _binding: FragmentMoreMovieInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference
    private val uid = Firebase.auth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        moreMovieInfoViewModel = ViewModelProvider(this)[MoreMovieInfoViewModel::class.java]
        _binding = FragmentMoreMovieInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        moreMovieInfoViewModel.moreMovie.observe(viewLifecycleOwner, {
            view?.findViewById<TextView>(R.id.film_name)?.text = it.title
            view?.findViewById<TextView>(R.id.description)?.text = it.overview
            view?.findViewById<TextView>(R.id.genres)?.text = it.getStringGenres()
            view?.findViewById<TextView>(R.id.rate_status)?.text = it.vote_average.toString()
            view?.findViewById<TextView>(R.id.duration_status)?.text = Time.intToStringTime(it.runtime)
            Picasso.get()
                .load(Image.getImageURL(it.poster_path, 185))
                .placeholder(R.drawable.ic_menu_slideshow)
                .into(view?.findViewById(R.id.logo))
        })
        return  root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addToWatching = view?.findViewById<Button>(R.id.add_to_watching)
        addToWatching.setOnClickListener {
            db = FirebaseDatabase.getInstance()
            users = db.getReference("films_id").child(uid.toString())
                .child(moreMovieInfoViewModel.moreMovie.value?.id.toString())
            users.setValue(moreMovieInfoViewModel.moreMovie.value).addOnFailureListener {
                eventAlert(view, "Ошибка добавления")
            }.addOnSuccessListener {
                eventAlert(view, "Считай что добавлено")
                view.findNavController().popBackStack()
            }
        }
        val toWatchListButton = view?.findViewById<Button>(R.id.to_watchlist)
        toWatchListButton.setOnClickListener {
            view.findNavController().navigate(R.id.watchListFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}