package com.fyp.privacyguard.login.ui.models

import java.lang.Exception

/**
 * Authentication result : success (user details) or error message.
 */
data class UserResult<T>(
    val success: T? = null,
    val error: Exception? = null
)