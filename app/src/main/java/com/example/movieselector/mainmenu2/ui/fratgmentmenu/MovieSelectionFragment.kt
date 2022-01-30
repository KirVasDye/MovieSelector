package com.example.movieselector.mainmenu2.ui.fratgmentmenu

import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.movieselector.R
import com.example.movieselector.databinding.FragmentDurationChooseBinding
import com.example.movieselector.databinding.FragmentMovieSelectionBinding
import com.example.movieselector.mainmenu.models.Image
import com.example.movieselector.mainmenu.models.Time
import com.example.movieselector.mainmenu2.exeptions.MaxPageEX
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels.MovieSelectionViewModel
import com.example.movieselector.toastwindow.eventAlert
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MovieSelectionFragment : Fragment() {

    private val TAG = "MovieSelectionFragment"
    private var _binding: FragmentMovieSelectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseDatabase
    private val uid = Firebase.auth.currentUser?.uid
    private lateinit var movieSelectionViewModel: MovieSelectionViewModel
    private lateinit var notInterestedButton: Button
    private lateinit var interestedButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        movieSelectionViewModel = ViewModelProvider(this)[MovieSelectionViewModel::class.java]
        _binding = FragmentMovieSelectionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        movieSelectionViewModel.movieList.observe(viewLifecycleOwner, {
            Log.d(TAG, "$it movieSelectionViewModel")
            if(it.isEmpty()){
                movieSelectionViewModel.next()
            }
            if(it.isNotEmpty()){
                Picasso.get()
                    .load(Image.getImageURL(it.first().poster_path, 185))
                    .placeholder(R.drawable.ic_menu_slideshow)
                    .into(view?.findViewById(R.id.poster))
                view?.findViewById<TextView>(R.id.text_about_film)?.text = it.first().overview
                view?.findViewById<TextView>(R.id.rating_status)?.text = it.first().vote_average.toString()
                view?.findViewById<TextView>(R.id.time_status)?.text = Time.intToStringTime(it.first().runtime)
                view?.findViewById<TextView>(R.id.text_about_film)?.append("\n-------------------\n${it.first().stringGenres()}")
            }
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notInterestedButton = view.findViewById(R.id.not_interesting)
        interestedButton = view.findViewById(R.id.interesting)
        notInterestedButton.setOnClickListener {
            movieSelectionViewModel.dropMovie()
        }
        interestedButton.setOnClickListener {
            movieSelectionViewModel.addToInteresting()
            view.findNavController().navigate(R.id.finalChooseStepFragment)
        }
    }

}