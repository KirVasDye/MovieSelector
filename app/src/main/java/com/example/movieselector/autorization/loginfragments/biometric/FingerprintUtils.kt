package com.example.movieselector.autorization.loginfragments.biometric

import android.content.Context
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import android.app.KeyguardManager

class FingerprintUtils {
    /**
     * Class for monitoring sensor states
     */
    enum class mSensorState {
        NOT_SUPPORTED,
        NOT_BLOCKED,
        NO_FINGERPRINTS,
        READY
    }

    fun checkFingerprintCompatibility(context: Context): Boolean {
        return FingerprintManagerCompat.from(context).isHardwareDetected
    }

    fun checkSensorState(context: Context): mSensorState? {
        return if (checkFingerprintCompatibility(context)) {
            val keyguardManager =
                context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (!keyguardManager.isKeyguardSecure) {
                return mSensorState.NOT_BLOCKED
            }
            if (!FingerprintManagerCompat.from(context).hasEnrolledFingerprints()) {
                mSensorState.NO_FINGERPRINTS
            } else mSensorState.READY
        } else {
            mSensorState.NOT_SUPPORTED
        }
    }

    fun isSensorStateAt(state: mSensorState, context: Context): Boolean {
        return checkSensorState(context) === state
    }
}