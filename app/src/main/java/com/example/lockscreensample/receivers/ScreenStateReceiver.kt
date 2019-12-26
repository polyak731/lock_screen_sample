package com.example.lockscreensample.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.lockscreensample.LockScreen
import com.example.lockscreensample.utils.LockUtils

class ScreenStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (!LockUtils.doesDeviceHaveSecuritySetup(context)) {
            context?.startActivity(Intent(context, LockScreen::class.java))
        }
    }
}