package com.example.movieselector.toastwindow

import android.view.View
import android.widget.Toast

fun eventAlert(view: View, string: String){
    Toast.makeText(view.context, string, Toast.LENGTH_SHORT).show()
}