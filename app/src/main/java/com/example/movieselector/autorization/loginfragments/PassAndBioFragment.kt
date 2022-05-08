package com.example.movieselector.autorization.loginfragments

import android.app.KeyguardManager
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.movieselector.R
import com.example.movieselector.autorization.NullFieldEX
import com.example.movieselector.autorization.RegisterLogic
import com.example.movieselector.mainmenu.MainMenuActivity
import com.example.movieselector.toastwindow.eventAlert
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.core.hardware.fingerprint.FingerprintManagerCompat



class PassAndBioFragment : Fragment() {
    private lateinit var settings: SharedPreferences
    private lateinit var email: String
    private lateinit var enter: Button
    private lateinit var biometricEnter: ImageButton
    private lateinit var password: EditText

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference

    val TAG = "PassAndBioFragment"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = context?.getSharedPreferences("email", Context.MODE_PRIVATE)!!
        email = settings.getString("email", "null.email").toString()
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Users")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pass_and_bio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.email_user)?.text = email
        enter = view.findViewById(R.id.log)
        biometricEnter = view.findViewById(R.id.bio)
        password = view.findViewById(R.id.password)


        enter.setOnClickListener { view ->
            try {
                auth.signInWithEmailAndPassword(
                    email,
                    RegisterLogic.register(password).text.toString()
                ).addOnSuccessListener {
                    eventAlert(view, "Вход выполнен")
                    val intent = Intent(context, MainMenuActivity::class.java)
                    intent.putExtra("uid", auth.currentUser!!.uid)
                    Log.d(TAG, "${auth.currentUser!!.uid}")
                    startActivity(intent)
                }.addOnFailureListener {
                    eventAlert(view, "Не удалось войти по причине ${it.message}")
                }
            } catch(e: NullFieldEX){
                eventAlert(view, "Не удалось войти по причине ${e.message}")
            }
        }

        biometricEnter.setOnClickListener { view ->
            eventAlert(view, "Use finger")
        }

    }

}