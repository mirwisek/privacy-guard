package com.fyp.privacyguard.core

import android.content.Context
import androidx.core.content.edit
import com.fyp.privacyguard.data.model.LoggedInUser
import com.fyp.privacyguard.log
import com.fyp.privacyguard.sharedPrefs

object SharedPrefsHelper {

    private const val KEY_USER_NAME = "name"
    private const val KEY_USER_PHONE = "phone"
    private const val KEY_USER_EMAIL = "email"

    fun saveUser(context: Context, user: LoggedInUser) {
        context.sharedPrefs.edit(true) {
            putString(KEY_USER_NAME, user.name)
            putString(KEY_USER_PHONE, user.phone)
            putString(KEY_USER_EMAIL, user.email)
        }
    }

    fun getUser(context: Context): LoggedInUser? {
        val user = LoggedInUser()
        with(context.sharedPrefs) {
            user.name = getString(KEY_USER_NAME, null)
            user.phone = getString(KEY_USER_PHONE, null)
            user.email = getString(KEY_USER_EMAIL, null)
        }
        if(user.email == null)
            return null
        return user
    }

    fun deleteUser(context: Context) {
        context.sharedPrefs.edit(true) {
            remove(KEY_USER_NAME)
            remove(KEY_USER_PHONE)
            remove(KEY_USER_EMAIL)
        }
    }

}