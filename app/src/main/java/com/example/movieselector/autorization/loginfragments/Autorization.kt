package com.example.movieselector.autorization.loginfragments

import android.os.Bundle
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
import com.example.movieselector.autorization.models.User
import com.example.movieselector.mainmenu.models.Duration
import com.example.movieselector.mainmenu.models.Page
import com.example.movieselector.mainmenu.models.Tag
import com.example.movieselector.toastwindow.eventAlert
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Autorization : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference
    private lateinit var page: DatabaseReference
    private lateinit var genres: DatabaseReference
    private lateinit var duration: DatabaseReference

    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var reenterPassword: EditText
    private lateinit var register: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Users")
        page = db.getReference("Pages")
        genres = db.getReference("GenresStatus")
        duration = db.getReference("DurationChoose")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_autorization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name = view.findViewById(R.id.nickname)
        email = view.findViewById(R.id.email_address)
        password = view.findViewById(R.id.text_password)
        reenterPassword = view.findViewById(R.id.text_reenter_password)
        register = view.findViewById(R.id.register)

        register.setOnClickListener { view ->
            try {
                if(RegisterLogic.register(password).text.toString() == RegisterLogic.register(reenterPassword).text.toString()) {
                    auth.createUserWithEmailAndPassword(
                        RegisterLogic.register(email).text.toString(),
                        RegisterLogic.register(password).text.toString()
                    ).addOnCompleteListener {
                        val user = User(
                            RegisterLogic.register(name).text.toString(),
                            RegisterLogic.register(email).text.toString(),
                            RegisterLogic.register(password).text.toString(),
                        )
                        users.child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .setValue(user)
                            .addOnCompleteListener {
                                page.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .setValue(Page())
                                genres.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .setValue(Tag())
                                duration.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .setValue(Duration())
                                eventAlert(view, "Регистрация успешна")
                                view.findNavController().navigate(R.id.login_main)
                            }.addOnFailureListener {
                                eventAlert(view, "Ошибка регистрации")
                            }
                    }
                } else{
                    eventAlert(view, "Пароли должны совпадать")
                }
            } catch (e: NullFieldEX){
                eventAlert(view, "Поля не должны быть пустыми!")
            }
        }
    }

}