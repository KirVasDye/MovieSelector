package com.example.movieselector.mainmenu2.ui.fratgmentmenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.movieselector.R
import com.example.movieselector.databinding.FragmentDurationChooseBinding
import com.example.movieselector.mainmenu.models.Duration
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels.DurationChooseViewModel
import com.example.movieselector.toastwindow.eventAlert
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class DurationChooseFragment : Fragment() {

    private var _binding: FragmentDurationChooseBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseDatabase
    private val uid = Firebase.auth.currentUser?.uid
    private lateinit var durationStatus: DatabaseReference
    private lateinit var hourChoose: SeekBar
    private lateinit var hour: TextView
    private lateinit var minuteChoose: SeekBar
    private lateinit var durationChooseViewModel: DurationChooseViewModel
    private lateinit var minute: TextView
    private lateinit var errorChoose: SeekBar
    private lateinit var error: TextView
    private lateinit var chooseButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseDatabase.getInstance()
        durationStatus = db.getReference("DurationChoose").child(uid.toString())
        
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        durationChooseViewModel = ViewModelProvider(this)[DurationChooseViewModel::class.java]
        _binding = FragmentDurationChooseBinding.inflate(inflater, container, false)
        val root: View = binding.root
        durationChooseViewModel.durationChoose.observe(viewLifecycleOwner, {
            view?.findViewById<SeekBar>(R.id.hour_choose)?.progress = it.hour
            view?.findViewById<TextView>(R.id.hour)?.text = it.hour.toString()
            view?.findViewById<SeekBar>(R.id.minute_choose)?.progress = it.minute
            view?.findViewById<TextView>(R.id.minute)?.text = it.minute.toString()
            view?.findViewById<SeekBar>(R.id.error_choose)?.progress = it.error
            view?.findViewById<TextView>(R.id.error)?.text = it.error.toString()
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hourChoose = view.findViewById(R.id.hour_choose)
        minuteChoose = view.findViewById(R.id.minute_choose)
        errorChoose = view.findViewById(R.id.error_choose)

        hour = view.findViewById(R.id.hour)
        minute = view.findViewById(R.id.minute)
        error = view.findViewById(R.id.error)

        chooseButton = view.findViewById(R.id.choose)

        hourChoose.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                hour.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                hour.text = seekBar?.progress.toString()
            }

        })
        minuteChoose.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                minute.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                minute.text = seekBar?.progress.toString()
            }

        })
        errorChoose.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                error.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                error.text = seekBar?.progress.toString()
            }

        })

        chooseButton.setOnClickListener {
            if(
                (Integer.parseInt(hour.text.toString()) == 0)
                && (Integer.parseInt(minute.text.toString()) < 30)
            ) {
                eventAlert(view, "Таких коротких фильмов нет")
            } else {
                durationStatus.setValue(
                    Duration(
                        hour = Integer.parseInt(hour.text.toString()),
                        minute = Integer.parseInt(minute.text.toString()),
                        error = Integer.parseInt(error.text.toString()),
                    )
                )
                eventAlert(view, "Данные установлены")
                view.findNavController().popBackStack()
            }
        }
    }

}