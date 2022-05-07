package com.example.movieselector.mainmenu.ui.fratgmentmenu

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieselector.R
import com.example.movieselector.databinding.FragmentUserListsBinding
import com.example.movieselector.mainmenu.models.MoreMovie
import com.example.movieselector.mainmenu.adapters.MoviesAdapter
import com.example.movieselector.mainmenu.models.Page
import com.example.movieselector.mainmenu.viewmodels.PageViewModel
import com.example.movieselector.mainmenu.viewmodels.UserListViewModel
import com.example.movieselector.toastwindow.eventAlert
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class UserListFragment : Fragment() {

    val TAG = "UserListFragment"
    private lateinit var setting: SharedPreferences
    private lateinit var userListViewModel: UserListViewModel
    private var _binding: FragmentUserListsBinding? = null
    private lateinit var clickListener: MoviesAdapter.ClickListener
    private lateinit var previousButton: ImageView
    private lateinit var nextButton: ImageView
    private lateinit var toStartButton: ImageView
    private lateinit var toEndButton: ImageView
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference
    private val uid = Firebase.auth.currentUser?.uid
    private lateinit var pageViewModel: PageViewModel

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Init")
        setting = context?.getSharedPreferences("id", Context.MODE_PRIVATE)!!

        clickListener = object : MoviesAdapter.ClickListener{
            override fun onItemClick(movie: MoreMovie) {
                val editor = setting.edit()
                editor.putString("id", movie.id.toString()).apply()
                view?.findNavController()?.navigate(R.id.moreMovieInfoFragment)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user_lists, container, false)

        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java]
        userListViewModel = ViewModelProvider(this)[UserListViewModel::class.java]
        _binding = FragmentUserListsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView = root.findViewById<RecyclerView>(R.id.films_list)

        userListViewModel.pageMax.observe(viewLifecycleOwner, {
            view?.findViewById<TextView>(R.id.page_max)?.text = "..$it"
        })

        pageViewModel.pageInfo.observe(viewLifecycleOwner, { page ->
            view?.findViewById<TextView>(R.id.page_status)?.text = page.value.toString()
            Log.d(TAG, page.value.toString())
        })
        userListViewModel.userData.observe(viewLifecycleOwner, {
            view?.findViewById<TextView>(R.id.name)?.text = it.name
        })

        recyclerView.layoutManager = LinearLayoutManager(context)
        userListViewModel.moreMovieList.observe(viewLifecycleOwner, {
            recyclerView.adapter = MoviesAdapter(it, clickListener)
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val goToWatchingButton = view.findViewById<Button>(R.id.watching)
        goToWatchingButton.setOnClickListener {
            view.findNavController().navigate(R.id.watchListFragment)
        }
        val goToSelectButton = view.findViewById<Button>(R.id.find_films)
        goToSelectButton.setOnClickListener {
            view.findNavController().navigate(R.id.selectionFragment)
        }
        val goToWantWatchingButton = view.findViewById<Button>(R.id.want_to_watching)
        goToWantWatchingButton.setOnClickListener {
            view.findNavController().navigate(R.id.wantToWatchingFragment)
        }

        previousButton = view.findViewById(R.id.left_button)
        nextButton = view.findViewById(R.id.right_button)
        toEndButton = view.findViewById(R.id.to_end)
        toStartButton = view.findViewById(R.id.to_start)
        nextButton.setOnClickListener {
            if(canNext()){
                loadNext()
            } else{
                eventAlert(view, "Дальше листать незя")
            }
        }
        previousButton.setOnClickListener {
            if(canPrev()){
                loadPrev()
            } else{
                eventAlert(view, "Дальше листать незя")
            }
        }
        toEndButton.setOnClickListener {
            if(!isEnd()){
                toEnd()
            } else{
                eventAlert(view, "Вы уже в конце")
            }
        }
        toStartButton.setOnClickListener {
            if(!isBegin()){
                toStart()
            } else{
                eventAlert(view, "Вы находитесь в начале")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        userListViewModel.users.removeEventListener(userListViewModel.refPages)
        userListViewModel.users.removeEventListener(userListViewModel.refUserData)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun canNext() = (pageViewModel.pageInfo.value?.value?.plus(1)!! <= userListViewModel.pageMax.value?.toInt()!!)
    private fun canPrev() = (pageViewModel.pageInfo.value?.value?.minus(1)!! >= 1)
    private fun isEnd() = (pageViewModel.pageInfo.value?.value!! == userListViewModel.pageMax.value?.toInt()!!)
    private fun isBegin() = (pageViewModel.pageInfo.value?.value!! == 1)

    private fun loadNext(){
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Pages").child(uid.toString())
        users.setValue(pageViewModel.pageInfo.value?.value?.plus(1)?.let { it1 -> Page(it1) })
    }
    private fun loadPrev(){
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Pages").child(uid.toString())
        users.setValue(pageViewModel.pageInfo.value?.value?.minus(1)?.let { it1 -> Page(it1) })
    }
    private fun toEnd(){
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Pages").child(uid.toString())
        users.setValue(userListViewModel.pageMax.value?.toInt()?.let { Page(it) })
    }
    private fun toStart(){
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Pages").child(uid.toString())
        users.setValue(Page())
    }

}