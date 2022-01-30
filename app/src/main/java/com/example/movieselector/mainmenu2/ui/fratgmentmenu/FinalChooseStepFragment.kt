package com.example.movieselector.mainmenu2.ui.fratgmentmenu

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.movieselector.databinding.FragmentFinalChooseStepBinding
import com.example.movieselector.databinding.FragmentMovieSelectionBinding
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.viewmodels.FinalChooseStepViewModel
import java.util.*
import android.widget.DatePicker
import android.text.format.DateUtils
import android.widget.TimePicker

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.provider.CalendarContract
import android.util.Log
import android.widget.Button
import com.example.movieselector.R
import com.example.movieselector.toastwindow.eventAlert
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat
import androidx.navigation.findNavController


class FinalChooseStepFragment : Fragment() {
    private val TAG = "FinalChooseStepFragment"
    private var _binding: FragmentFinalChooseStepBinding? = null
    private val binding get() = _binding!!
    private lateinit var finalChooseStepViewModel: FinalChooseStepViewModel
    var dateAndTime: Calendar = Calendar.getInstance()
    private lateinit var setTimeButton: Button
    private lateinit var setDateButton: Button
    private lateinit var setInCalendar: Button
    private lateinit var goToMainMenuButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        finalChooseStepViewModel = ViewModelProvider(this)[FinalChooseStepViewModel::class.java]
        _binding = FragmentFinalChooseStepBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDateButton = view.findViewById(R.id.date_picker)
        setTimeButton = view.findViewById(R.id.time_picker)
        setInCalendar = view.findViewById(R.id.set_in_calendar)
        goToMainMenuButton = view.findViewById(R.id.in_main_menu)
        setTimeButton.setOnClickListener {
            setTime(view)
        }
        setDateButton.setOnClickListener {
            setDate(view)
        }
        setInCalendar.setOnClickListener {
            Log.d(TAG, "${dateAndTime.get(Calendar.YEAR)} year " +
                    "${dateAndTime.get(Calendar.MONTH)} month " +
                    "${dateAndTime.get(Calendar.DAY_OF_MONTH)} day")
            Log.d(TAG, "${dateAndTime.get(Calendar.HOUR_OF_DAY)} hour " +
                    "${dateAndTime.get(Calendar.MINUTE)} minute")
            if(isStoragePermissionGranted()) {
                /*finalChooseStepViewModel.setInCalendar(dateAndTime, cv, cr!!)*/
                finalChooseStepViewModel.interestingFilm.observe(viewLifecycleOwner, {
                    val startEvent: Long = dateAndTime.timeInMillis
                    val intent = Intent(Intent.ACTION_INSERT)
                    intent.data = CalendarContract.Events.CONTENT_URI
                    intent.putExtra(CalendarContract.Events.TITLE, "Watching film")
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, "You want watching ${it.title}")
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "In your phone")
                    intent.putExtra(CalendarContract.Events.DTSTART, startEvent)
                    intent.putExtra(CalendarContract.Events.ALL_DAY, false)
                    startActivity(intent)
                    /*val startEvent: Long = dateAndTime.timeInMillis
                    cv.put(CalendarContract.Events.TITLE, "Watching film")
                    cv.put(CalendarContract.Events.DESCRIPTION, "You want watching ${it.title}")
                    cv.put(CalendarContract.Events.EVENT_LOCATION, "In your phone")
                    cv.put(CalendarContract.Events.DTSTART, startEvent)
                    cv.put(CalendarContract.Events.CALENDAR_ID, 3)
                    cv.put(CalendarContract.Events.EVENT_TIMEZONE, dateAndTime.timeZone.id)
                    val uri = cr?.insert(CalendarContract.Events.CONTENT_URI, cv)*/
                })
                eventAlert(view, "Установлено")
            }
        }
        goToMainMenuButton.setOnClickListener {
            view.findNavController().navigate(R.id.userLists)
        }

    }

    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_CALENDAR
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_CALENDAR),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }
    }

    private fun setDate(v: View){
        DatePickerDialog(
            requireContext(), d,
            dateAndTime.get(Calendar.YEAR),
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH),
        ).show()
    }

    private fun setTime(v: View) {
        TimePickerDialog(
            requireContext(), t,
            dateAndTime[Calendar.HOUR_OF_DAY],
            dateAndTime[Calendar.MINUTE], true
        ).show()
    }

    var t =
        OnTimeSetListener { view, hourOfDay, minute ->
            dateAndTime[Calendar.HOUR_OF_DAY] = hourOfDay
            dateAndTime[Calendar.MINUTE] = minute
        }

    var d =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateAndTime[Calendar.YEAR] = year
            dateAndTime[Calendar.MONTH] = monthOfYear
            dateAndTime[Calendar.DAY_OF_MONTH] = dayOfMonth
        }

}