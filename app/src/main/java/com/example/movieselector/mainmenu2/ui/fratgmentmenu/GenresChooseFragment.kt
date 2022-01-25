package com.example.movieselector.mainmenu2.ui.fratgmentmenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.movieselector.R
import com.example.movieselector.databinding.FragmentGenresChooseBinding
import com.example.movieselector.databinding.FragmentSelectionBinding
import com.example.movieselector.mainmenu.models.Tag
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels.GenresChooseViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class GenresChooseFragment : Fragment() {
    private var _binding: FragmentGenresChooseBinding? = null
    private val binding get() = _binding!!
    private lateinit var genresChooseViewModel: GenresChooseViewModel
    private lateinit var db: FirebaseDatabase
    private lateinit var genresStatus: DatabaseReference
    private val uid = Firebase.auth.currentUser?.uid
    private lateinit var goBack: Button
    val TAG = "GenresChooseFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseDatabase.getInstance()
        genresStatus = db.getReference("GenresStatus").child(uid.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        genresChooseViewModel = ViewModelProvider(this)[GenresChooseViewModel::class.java]
        _binding = FragmentGenresChooseBinding.inflate(inflater, container, false)
        val root: View = binding.root
        genresChooseViewModel.listTags.observe(viewLifecycleOwner, { list->
            var spinner = view?.findViewById<Spinner>(R.id.genres_choose)!!
            var adapter: ArrayAdapter<Tag> =
                ArrayAdapter(
                    requireContext(),
                    R.layout.spiner_item,
                    list
                )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner?.adapter = adapter
            spinner.setSelection(findIndexElement(list, genresChooseViewModel.tagStatus.value!!))
            Log.d(TAG, "${list.indexOf(genresChooseViewModel.tagStatus.value)}")
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    itemSelected: View, selectedItemPosition: Int, selectedId: Long
                ) {
                    genresStatus.setValue(
                        genresChooseViewModel.listTags.value?.get(selectedItemPosition)
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goBack = view.findViewById(R.id.go_back)
        goBack.setOnClickListener {
            view.findNavController().popBackStack()
        }
    }

    private fun findIndexElement(arr: MutableList<Tag>, tag: Tag): Int{
        var res = -1
        for(i in 0 until arr.size){
            if(arr[i].id == tag.id){
                res = i
                break
            }
        }
        return res
    }
}