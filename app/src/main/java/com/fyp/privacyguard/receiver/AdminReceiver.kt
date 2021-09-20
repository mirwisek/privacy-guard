package com.fyp.privacyguard.receiver

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.os.UserHandle
import com.fyp.privacyguard.log
import com.fyp.privacyguard.service.PhoneLogService
import com.fyp.privacyguard.toast

class AdminReceiver: DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        log("Has been enabled")
        super.onEnabled(context, intent)
    }

    override fun onDisabled(context: Context, intent: Intent) {
        log("Disabled")
        super.onDisabled(context, intent)
    }

    override fun onPasswordFailed(context: Context, intent: Intent, user: UserHandle) {
        val time = System.currentTimeMillis()
        log("Password failed $time")
        context.toast("Password failed $time")
        context.startLogService(PhoneLogService.ACTION_PASSWORD_FAILED)
        super.onPasswordFailed(context, intent, user)
    }

    override fun onPasswordSucceeded(context: Context, intent: Intent, user: UserHandle) {
        val time = System.currentTimeMillis()
        log("Password succeeded $time")
        context.toast("Password succeeded $time")
        context.startLogService(PhoneLogService.ACTION_PASSWORD_SUCCEEDED)
        super.onPasswordSucceeded(context, intent, user)
    }

    private fun Context.startLogService(action: String) {
        val i = Intent(this, PhoneLogService::class.java)
        i.action = action
        startService(i)
    }

}