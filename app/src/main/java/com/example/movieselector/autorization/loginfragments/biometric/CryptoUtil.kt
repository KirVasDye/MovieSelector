package com.example.movieselector.autorization.loginfragments.biometric

import android.os.Build
import javax.crypto.Cipher
import android.security.keystore.KeyProperties
import android.security.keystore.KeyGenParameterSpec
import androidx.annotation.RequiresApi
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.util.Base64
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import java.security.spec.InvalidKeySpecException
import java.security.spec.MGF1ParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource


class CryptoUtil {
    private val TAG: String = CryptoUtil::class.java.simpleName

    private val KEY_ALIAS = "key_for_pin"
    private val KEY_STORE = "AndroidKeyStore"
    private val TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"

    private lateinit var sKeyStore: KeyStore
    private lateinit var sKeyPairGenerator: KeyPairGenerator
    private lateinit var sCipher: Cipher

    @RequiresApi(Build.VERSION_CODES.M)
    fun encode(inputString: String): String? {
        try {
            if (prepare() && initCipher(Cipher.ENCRYPT_MODE)) {
                val bytes = sCipher!!.doFinal(inputString.toByteArray())
                return Base64.encodeToString(bytes, Base64.NO_WRAP)
            }
        } catch (exception: IllegalBlockSizeException) {
            exception.printStackTrace()
        } catch (exception: BadPaddingException) {
            exception.printStackTrace()
        }
        return null
    }

    fun decode(encodedString: String?, cipher: Cipher): String? {
        try {
            val bytes: ByteArray = Base64.decode(encodedString, Base64.NO_WRAP)
            return String(cipher.doFinal(bytes))
        } catch (exception: IllegalBlockSizeException) {
            exception.printStackTrace()
        } catch (exception: BadPaddingException) {
            exception.printStackTrace()
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun prepare(): Boolean {
        return getKeyStore() && getCipher() && getKey()
    }

    private fun getKeyStore(): Boolean {
        try {
            sKeyStore = KeyStore.getInstance(KEY_STORE)
            sKeyStore.load(null)
            return true
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        }
        return false
    }

    private fun getKeyPairGenerator(): Boolean {
        try {
            sKeyPairGenerator =
                KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEY_STORE)
            return true
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        }
        return false
    }

    private fun getCipher(): Boolean {
        try {
            sCipher = Cipher.getInstance(TRANSFORMATION)
            return true
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getKey(): Boolean {
        try {
            return sKeyStore.containsAlias(KEY_ALIAS) || generateNewKey()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateNewKey(): Boolean {
        if (getKeyPairGenerator()) {
            try {
                sKeyPairGenerator.initialize(
                    KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                        .setUserAuthenticationRequired(true)
                        .build()
                )
                sKeyPairGenerator.generateKeyPair()
                return true
            } catch (e: InvalidAlgorithmParameterException) {
                e.printStackTrace()
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initCipher(mode: Int): Boolean {
        try {
            sKeyStore.load(null)
            when (mode) {
                Cipher.ENCRYPT_MODE -> initEncodeCipher(mode)
                Cipher.DECRYPT_MODE -> initDecodeCipher(mode)
                else -> return false //this cipher is only for encode\decode
            }
            return true
        } catch (exception: KeyPermanentlyInvalidatedException) {
            deleteInvalidKey()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: UnrecoverableKeyException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        }
        return false
    }

    @Throws(
        KeyStoreException::class,
        NoSuchAlgorithmException::class,
        UnrecoverableKeyException::class,
        InvalidKeyException::class
    )
    private fun initDecodeCipher(mode: Int) {
        val key: PrivateKey = sKeyStore.getKey(KEY_ALIAS, null) as PrivateKey
        sCipher.init(mode, key)
    }

    @Throws(
        KeyStoreException::class,
        InvalidKeySpecException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class
    )
    private fun initEncodeCipher(mode: Int) {
        val key: PublicKey = sKeyStore.getCertificate(KEY_ALIAS).publicKey

        // workaround for using public key
        // from https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.html
        val unrestricted: PublicKey = KeyFactory.getInstance(key.getAlgorithm())
            .generatePublic(X509EncodedKeySpec(key.getEncoded()))
        // from https://code.google.com/p/android/issues/detail?id=197719
        val spec =
            OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT)
        sCipher.init(mode, unrestricted, spec)
    }

    fun deleteInvalidKey() {
        if (getKeyStore()) {
            try {
                sKeyStore.deleteEntry(KEY_ALIAS)
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getCryptoObject(): FingerprintManagerCompat.CryptoObject? {
        return if (prepare() && initCipher(Cipher.DECRYPT_MODE)) {
            FingerprintManagerCompat.CryptoObject(sCipher)
        } else null
    }

}