package com.example.movieselector.mainmenu2.ui.fratgmentmenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.movieselector.R
import com.example.movieselector.databinding.FragmentDurationChooseBinding
import com.example.movieselector.databinding.FragmentMovieSelectionBinding
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels.MovieSelectionViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MovieSelectionFragment : Fragment() {

    private val TAG = "MovieSelectionFragment"
    private var _binding: FragmentMovieSelectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseDatabase
    private val uid = Firebase.auth.currentUser?.uid
    private lateinit var movieSelectionViewModel: MovieSelectionViewModel


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
        movieSelectionViewModel.durationChoose.observe(viewLifecycleOwner, {
            Log.d(TAG, it.toString())
        })
        return root
    }

}