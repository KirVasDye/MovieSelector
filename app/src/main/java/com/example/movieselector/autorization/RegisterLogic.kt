package com.example.movieselector.autorization

import android.text.TextUtils
import android.widget.TextView
import java.lang.Exception

object RegisterLogic {
    fun register(text: TextView): TextView {
        if(TextUtils.isEmpty(text.text.toString())){
            throw NullFieldEX()
        }
        return text
    }
}

class NullFieldEX : Exception() {
    override val message: String?
        get() = "Null Field"
}