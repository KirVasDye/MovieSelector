package com.example.movieselector.mainmenu

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.movieselector.R
import com.example.movieselector.databinding.ActivityMainMenuBinding


class MainMenuActivity : AppCompatActivity() {

    val TAG = "MainMenuActivity"
    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)
        Log.d(TAG, "Start3")
    }
}