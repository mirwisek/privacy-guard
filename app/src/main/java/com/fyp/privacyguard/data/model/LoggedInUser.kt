package com.fyp.privacyguard.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    var email: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var password: String? = null
)