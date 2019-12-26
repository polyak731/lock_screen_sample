package com.example.lockscreensample

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.lockscreensample.receivers.DeviceAdmin
import com.example.lockscreensample.receivers.ScreenStateReceiver
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    companion object {
        const val RESULT_ENABLE = 342
    }

    var policyManager: DevicePolicyManager? = null
    var deviceAdmin: ComponentName? = null
    var receiver: ScreenStateReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        policyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        deviceAdmin = ComponentName(
            this,
            DeviceAdmin::class.java
        )

        lock_screen_button.setOnClickListener {
            lockScreen()
        }
        enable_admin_button.setOnClickListener {
            enableAdmin()
        }
        disable_admin_button.setOnClickListener {
            disableAdmin()
        }
        handleButtonsVisibility()

        try {
            receiver = ScreenStateReceiver()
            receiver?.let {
                registerReceiver(it, IntentFilter(Intent.ACTION_SCREEN_ON))
            }
        } catch (ex: Exception) {
            /**NOP*/
        }
    }

    override fun onDestroy() {
        try {
            receiver?.let {
                unregisterReceiver(it)
            }
        } catch (ex: Exception) {
            /**NOP*/
        }
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RESULT_ENABLE -> {
                if (resultCode === Activity.RESULT_OK) {
                    Log.i("DeviceAdminSample", "Admin enabled!")
                    handleButtonsVisibility()
                } else {
                    Log.i("DeviceAdminSample", "Admin enable FAILED!")
                }
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun lockScreen() {
        policyManager?.lockNow()
    }

    private fun handleButtonsVisibility() {
        val adminEnabled = deviceAdmin?.let { policyManager?.isAdminActive(it) } ?: false
        enable_admin_button.isEnabled = !adminEnabled
        disable_admin_button.isEnabled = adminEnabled
    }

    private fun enableAdmin() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(
            DevicePolicyManager.EXTRA_DEVICE_ADMIN,
            deviceAdmin
        )
        intent.putExtra(
            DevicePolicyManager.EXTRA_ADD_EXPLANATION,
            "Additional text explaining why this needs to be added."
        )
        startActivityForResult(intent, RESULT_ENABLE)
    }

    private fun disableAdmin() {
        deviceAdmin?.let { policyManager?.removeActiveAdmin(it) }
        handleButtonsVisibility()
    }
}
