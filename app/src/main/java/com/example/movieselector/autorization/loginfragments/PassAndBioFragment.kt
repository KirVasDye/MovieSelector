package com.example.movieselector.autorization.loginfragments


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import com.example.movieselector.R
import com.example.movieselector.autorization.NullFieldEX
import com.example.movieselector.autorization.RegisterLogic
import com.example.movieselector.mainmenu.MainMenuActivity
import com.example.movieselector.toastwindow.eventAlert
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import com.example.movieselector.autorization.loginfragments.biometric.CryptoUtil
import javax.crypto.Cipher
import com.example.movieselector.autorization.loginfragments.biometric.FingerprintUtils
import com.example.movieselector.autorization.loginfragments.biometric.FingerprintUtils.isSensorStateAt


class PassAndBioFragment : Fragment() {
    private lateinit var settings: SharedPreferences
    private lateinit var mPreferences: SharedPreferences
    private lateinit var email: String
    private lateinit var enter: Button
    private lateinit var biometricEnter: ImageButton
    private lateinit var password: EditText

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference

    private val PIN = "pin"

    private var mFingerprintHelper: FingerprintHelper? = null

    val TAG = "PassAndBioFragment"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = context?.getSharedPreferences("email", Context.MODE_PRIVATE)!!
        email = settings.getString("email", "null.email").toString()
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Users")

        mPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pass_and_bio, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.email_user)?.text = email
        enter = view.findViewById(R.id.log)
        biometricEnter = view.findViewById(R.id.bio)
        password = view.findViewById(R.id.password)


        enter.setOnClickListener { view ->
            signIn(email, password, view)
        }

        biometricEnter.setOnClickListener { view ->
            prepareLogin()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        if (mPreferences.contains(PIN)) {
            prepareSensor();
        }
    }

    override fun onStop() {
        super.onStop()
        if (mFingerprintHelper != null) {
            mFingerprintHelper!!.cancel();
        }
    }

    private fun signIn(email: String, password: TextView, view: View){
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun prepareLogin() {
        val pin: String = password.text.toString()
        if (pin.isNotEmpty()) {
            savePin(pin)
        } else {
            Toast.makeText(context, "pin is empty", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun savePin(pin: String) {
        if (FingerprintUtils.isSensorStateAt(FingerprintUtils.mSensorState.READY, requireContext())) {
            val encoded: String? = CryptoUtil.encode(pin)
            mPreferences.edit().putString(PIN, encoded).apply()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun prepareSensor() {
        if (isSensorStateAt(FingerprintUtils.mSensorState.READY, requireContext())) {
            val cryptoObject: FingerprintManagerCompat.CryptoObject? = CryptoUtil.getCryptoObject()
            if (cryptoObject != null) {
                Toast.makeText(context, "use fingerprint to login", Toast.LENGTH_LONG).show()
                mFingerprintHelper = FingerprintHelper(requireContext())
                mFingerprintHelper!!.startAuth(cryptoObject)
            } else {
                mPreferences.edit().remove(PIN).apply()
                Toast.makeText(
                    context,
                    "new fingerprint enrolled. enter pin again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    inner class FingerprintHelper(private val mContext: Context) : FingerprintManagerCompat.AuthenticationCallback() {
        private var mCancellationSignal: CancellationSignal? = null

        fun startAuth(cryptoObject: FingerprintManagerCompat.CryptoObject?) {
            mCancellationSignal = CancellationSignal()
            val manager = FingerprintManagerCompat.from(mContext)
            manager.authenticate(cryptoObject, 0, mCancellationSignal, this, null)
        }

        fun cancel() {
            if (mCancellationSignal != null) {
                mCancellationSignal!!.cancel()
            }
        }

        override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
            Toast.makeText(mContext, errString, Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
            Toast.makeText(mContext, helpString, Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
            val cipher: Cipher? = result!!.cryptoObject.cipher
            val encoded: String? = mPreferences.getString(PIN, null)
            val decoded: String? = CryptoUtil.decode(encoded, cipher!!)
            password.setText(decoded)
            signIn(email, password, view!!)
            Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationFailed() {
            Toast.makeText(mContext, "try again", Toast.LENGTH_SHORT).show()
        }

    }

}