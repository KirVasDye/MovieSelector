package com.example.movieselector.mainmenu2.ui.fratgmentmenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.movieselector.R
import com.example.movieselector.databinding.FragmentDurationChooseBinding
import com.example.movieselector.databinding.FragmentFinalChooseStepBinding
import com.example.movieselector.databinding.FragmentWantToWatchingBinding
import com.example.movieselector.mainmenu.models.Image
import com.example.movieselector.mainmenu.models.Time
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels.WantToWatchingView
import com.example.movieselector.toastwindow.eventAlert
import com.squareup.picasso.Picasso

class WantToWatchingFragment : Fragment() {
    private val TAG = "WantToWatchingFragment"
    private var _binding: FragmentWantToWatchingBinding? = null
    private val binding get() = _binding!!
    private lateinit var wantToWatchingView: WantToWatchingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        wantToWatchingView = ViewModelProvider(this)[WantToWatchingView::class.java]
        _binding = FragmentWantToWatchingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        wantToWatchingView.interestingFilm.observe(viewLifecycleOwner, {
            if(it.id != 0){
                Picasso.get()
                    .load(Image.getImageURL(it.poster_path, 185))
                    .placeholder(R.drawable.ic_menu_slideshow)
                    .into(view?.findViewById(R.id.poster))
                view?.findViewById<TextView>(R.id.text_about_film)?.text = it.title
                view?.findViewById<TextView>(R.id.text_about_film)?.append("\n-------------------\n${it.overview}")
                view?.findViewById<TextView>(R.id.rating_status)?.text = it.vote_average.toString()
                view?.findViewById<TextView>(R.id.time_status)?.text = Time.intToStringTime(it.runtime)
                view?.findViewById<TextView>(R.id.text_about_film)?.append("\n-------------------\n${it.stringGenres()}")
            } else {
                eventAlert(requireView(), "Вы не выбрали фильм")
                view?.findNavController()?.popBackStack()
            }
        })
        return root
    }
}