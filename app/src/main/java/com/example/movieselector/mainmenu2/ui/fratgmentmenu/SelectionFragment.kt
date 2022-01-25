package com.example.movieselector.mainmenu2.ui.fratgmentmenu

import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.movieselector.R
import com.example.movieselector.databinding.FragmentMoreMovieInfoBinding
import com.example.movieselector.databinding.FragmentSelectionBinding
import com.example.movieselector.databinding.FragmentUserListsBinding
import com.example.movieselector.mainmenu.models.Tag
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels.MoreMovieInfoViewModel
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels.SelectionViewModel
import com.example.movieselector.toastwindow.eventAlert
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SelectionFragment : Fragment() {
    private var _binding: FragmentSelectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectionViewModel: SelectionViewModel
    private lateinit var goToGenresChoose: Button
    private lateinit var clearAll: Button
    private lateinit var goToDurationChoose: Button
    private lateinit var db: FirebaseDatabase
    private lateinit var genresStatus: DatabaseReference
    private lateinit var duration: DatabaseReference
    private val uid = Firebase.auth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseDatabase.getInstance()
        genresStatus = db.getReference("GenresStatus").child(uid.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        selectionViewModel = ViewModelProvider(this)[SelectionViewModel::class.java]
        _binding = FragmentSelectionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        selectionViewModel.tagStatus.observe(viewLifecycleOwner, {
            if(it.id == 0){
                view?.findViewById<ImageView>(R.id.genres_done)?.visibility = View.INVISIBLE
                view?.findViewById<ImageView>(R.id.genres_disable)?.visibility = View.VISIBLE
            } else {
                view?.findViewById<ImageView>(R.id.genres_done)?.visibility = View.VISIBLE
                view?.findViewById<ImageView>(R.id.genres_disable)?.visibility = View.INVISIBLE
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clearAll = view.findViewById(R.id.del_all)
        goToGenresChoose = view.findViewById(R.id.select_genres_button)
        goToDurationChoose = view.findViewById(R.id.select_duration_button)
        clearAll.setOnClickListener {
            genresStatus.setValue(Tag())
            eventAlert(view, "Очищено")
        }
        goToGenresChoose.setOnClickListener {
            view.findNavController().navigate(R.id.genresChooseFragment)
        }
        goToDurationChoose.setOnClickListener {
            view.findNavController().navigate(R.id.durationChooseFragment)
        }
    }

}