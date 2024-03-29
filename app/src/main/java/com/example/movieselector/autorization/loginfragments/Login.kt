package com.example.movieselector.autorization.loginfragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import com.example.movieselector.R
import com.example.movieselector.autorization.NullFieldEX
import com.example.movieselector.autorization.RegisterLogic
import com.example.movieselector.mainmenu.MainMenuActivity
import com.example.movieselector.toastwindow.eventAlert
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.SignInMethodQueryResult

import com.google.android.gms.tasks.OnCompleteListener
import java.lang.IllegalArgumentException


class Login : Fragment() {

    val TAG = "Login"
    private lateinit var setting: SharedPreferences
    private lateinit var userName: EditText
    private lateinit var enter: Button
    private lateinit var register: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Users")
        setting = context?.getSharedPreferences("email", Context.MODE_PRIVATE)!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userName = view.findViewById(R.id.email_address)
        enter = view.findViewById(R.id.enter)
        register = view.findViewById(R.id.register)

        register.setOnClickListener{
            view.findNavController().navigate(R.id.autorization)
        }

        enter.setOnClickListener{ view ->

            try {
                auth.fetchSignInMethodsForEmail(userName.text.toString())
                    .addOnCompleteListener { task ->
                        val isNewUser = task.result.signInMethods?.isEmpty()
                        if (isNewUser == true) {
                            eventAlert(view, "email not exist")
                        } else {
                            val editor = setting.edit()
                            editor.putString("email", userName.text.toString()).apply()
                            eventAlert(view, "email is exist")
                            view.findNavController().navigate(R.id.passAndBioFragment)
                        }
                    }
            } catch (e: NullFieldEX){
                eventAlert(view, "Не удалось войти по причине ${e.message}")
            } catch (ex: IllegalArgumentException){
                eventAlert(view, "Не удалось войти по причине ${ex.message}")
            }
            /*try {
                auth.signInWithEmailAndPassword(
                    RegisterLogic.register(userName).text.toString(),
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
            }*/
        }
    }

}