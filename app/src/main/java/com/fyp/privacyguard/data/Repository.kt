package com.fyp.privacyguard.data

import android.content.Context
import com.fyp.privacyguard.core.SharedPrefsHelper
import com.fyp.privacyguard.data.model.LoggedInUser
import com.fyp.privacyguard.network.ApiHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class Repository {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun login(email: String, password: String, result: (Result<LoggedInUser>) -> Unit) {

        ApiHelper.login(LoggedInUser(email=email, password = password)) { res ->
            res.fold(
                onSuccess = { user ->
                    try {
                        setLoggedInUser(user)
                        result.invoke(Result.Success(user))
                    } catch (e: Exception) {
                        result.invoke(Result.Error(e))
                    }
                },
                onFailure = {
                    result.invoke(Result.Error(Exception(it)))
                }
            )
        }
    }

    fun forgetPassword(email: String, result: (Result<Boolean>) -> Unit) {
        if(email.isNotBlank()) {

            ApiHelper.forgetPassword(email) { res ->
                res.fold(
                    onSuccess = {
                        result.invoke(Result.Success(true))
                    },
                    onFailure = {
                        result.invoke(Result.Error(Exception(it)))
                    }
                )
            }
        }
    }

    fun register(user: LoggedInUser, result: (Result<LoggedInUser>) -> Unit) {

        ApiHelper.signup(user) { res ->
            res.fold(
                onSuccess = {
                    setLoggedInUser(user)
                    result.invoke(Result.Success(user))
                },
                onFailure = {
                    result.invoke(Result.Error(Exception(it)))
                }
            )
        }
    }

    fun setLoggedInUser(loggedInUser: LoggedInUser?) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}