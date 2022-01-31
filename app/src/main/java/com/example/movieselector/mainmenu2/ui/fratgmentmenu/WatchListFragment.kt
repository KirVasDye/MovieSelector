package com.example.movieselector.mainmenu2.ui.fratgmentmenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieselector.R
import com.example.movieselector.databinding.FragmentWatchListBinding
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.adapters.MoviesWatchingAdapter
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels.WatchListViewModel
import com.example.movieselector.toastwindow.eventAlert
import android.widget.AdapterView.OnItemSelectedListener


class WatchListFragment : Fragment() {

    private lateinit var watchListViewModel: WatchListViewModel
    private var _binding: FragmentWatchListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        watchListViewModel = ViewModelProvider(this)[WatchListViewModel::class.java]
        _binding = FragmentWatchListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView = root.findViewById<RecyclerView>(R.id.list_films)
        recyclerView.layoutManager = LinearLayoutManager(context)
        watchListViewModel.moreMovieList.observe(viewLifecycleOwner, {
            view?.findViewById<RecyclerView>(R.id.list_films)?.adapter = MoviesWatchingAdapter(it)
        })
        return  root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO replace condition
        watchListViewModel.moreMovieList.observe(viewLifecycleOwner, {
            if(it.isEmpty()){
                eventAlert(view, "Список фильмов пуст")
                view.findNavController().popBackStack()
            }
        })
        val spinner = view.findViewById<Spinner>(R.id.order_by)
        val adapter: ArrayAdapter<CharSequence> =
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.order_strings,
                android.R.layout.simple_spinner_item
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                when(selectedItemPosition){
                    0 -> {
                        watchListViewModel.moreMovieList.observe(viewLifecycleOwner, {
                            it.sortBy { it.title }
                            view.findViewById<RecyclerView>(R.id.list_films)?.adapter = MoviesWatchingAdapter(it)
                        })
                    }
                    1 -> {
                        watchListViewModel.moreMovieList.observe(viewLifecycleOwner, {
                            it.sortByDescending { it.title }
                            view.findViewById<RecyclerView>(R.id.list_films)?.adapter = MoviesWatchingAdapter(it)
                        })
                    }
                    3 -> {
                        watchListViewModel.moreMovieList.observe(viewLifecycleOwner, {
                            it.sortBy { it.runtime }
                            view.findViewById<RecyclerView>(R.id.list_films)?.adapter = MoviesWatchingAdapter(it)
                        })
                    }
                    2 -> {
                        watchListViewModel.moreMovieList.observe(viewLifecycleOwner, {
                            it.sortByDescending { it.runtime }
                            view.findViewById<RecyclerView>(R.id.list_films)?.adapter = MoviesWatchingAdapter(it)
                        })
                    }
                    5 -> {
                        watchListViewModel.moreMovieList.observe(viewLifecycleOwner, {
                            it.sortBy { it.vote_average }
                            view.findViewById<RecyclerView>(R.id.list_films)?.adapter = MoviesWatchingAdapter(it)
                        })
                    }
                    4 -> {
                        watchListViewModel.moreMovieList.observe(viewLifecycleOwner, {
                            it.sortByDescending { it.vote_average }
                            view.findViewById<RecyclerView>(R.id.list_films)?.adapter = MoviesWatchingAdapter(it)
                        })
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onPause() {
        super.onPause()
        watchListViewModel.users.removeEventListener(watchListViewModel.refMovie)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}