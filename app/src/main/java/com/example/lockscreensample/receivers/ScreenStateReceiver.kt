package com.example.lockscreensample.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.lockscreensample.LockScreen

class ScreenStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.startActivity(Intent(context, LockScreen::class.java))
    }
}