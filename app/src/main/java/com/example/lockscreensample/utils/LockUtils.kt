package com.example.lockscreensample.utils

import android.app.KeyguardManager
import android.content.ContentResolver
import android.content.Context
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException


object LockUtils {

    /**
     *
     * Checks to see if the lock screen is set up with either a PIN / PASS / PATTERN
     *
     *
     * For Api 16+
     *
     * @return true if PIN, PASS or PATTERN set, false otherwise.
     */
    fun doesDeviceHaveSecuritySetup(context: Context): Boolean {
        return isPatternSet(context) || isPassOrPinSet(context)
    }

    /**
     * @param context
     * @return true if pattern set, false if not (or if an issue when checking)
     */
    private fun isPatternSet(context: Context): Boolean {
        val cr: ContentResolver = context.getContentResolver()
        return try {
            val lockPatternEnable: Int =
                Settings.Secure.getInt(cr, Settings.Secure.LOCK_PATTERN_ENABLED)
            lockPatternEnable == 1
        } catch (e: SettingNotFoundException) {
            false
        }
    }

    /**
     * @param context
     * @return true if pass or pin set
     */
    private fun isPassOrPinSet(context: Context): Boolean {
        val keyguardManager =
            context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager //api 16+
        return keyguardManager.isKeyguardSecure
    }
}