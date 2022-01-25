package com.example.movieselector.mainmenu2.ui.fratgmentmenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movieselector.R
import com.example.movieselector.databinding.FragmentDurationChooseBinding
import com.example.movieselector.databinding.FragmentGenresChooseBinding

class DurationChooseFragment : Fragment() {

    private var _binding: FragmentDurationChooseBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDurationChooseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

}