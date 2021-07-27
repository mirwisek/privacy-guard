package com.fyp.privacyguard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fyp.privacyguard.login.ui.FullscreenActivity
import com.google.android.material.snackbar.Snackbar

fun Activity.switchActivity(targetActivity: Class<*>) {
    val intent = Intent(this, targetActivity)
    startActivity(intent)
    this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    this.finish()
}

fun Fragment.toast(msg: String, len: Int = Toast.LENGTH_SHORT) {
    requireContext().toast(msg, len)
}

fun Context.toast(msg: String, len: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, len).show()
}

fun log(msg: String) {
    Log.d("🌀❤💕😎🤷 ffnet :: ‍", msg)
}

fun View?.showSnackbar(
    text: String, length: Int = Snackbar.LENGTH_SHORT,
    animationMode: Int = Snackbar.ANIMATION_MODE_SLIDE,
    textColor: Int = R.color.white,
    color: Int = R.color.light_blue_900
) {
    this?.let { view ->
        val snackbar =
            Snackbar.make(view, text, length)
                .setAnimationMode(animationMode)
        snackbar.view.apply {
            setBackgroundColor(view.context.getColor(color))
        }
        snackbar.setTextColor(view.context.getColor(textColor))
        snackbar.show()
    }
}

fun Context.getColorCompat(color: Int): Int {
    return ContextCompat.getColor(this, color)
}

val Context.sharedPrefs: SharedPreferences
    get() {
        return getSharedPreferences("privacy-guard-shared-prefs", Context.MODE_PRIVATE)
    }