package com.fyp.privacyguard.login.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fyp.privacyguard.data.Repository
import com.fyp.privacyguard.data.Result

import com.fyp.privacyguard.R
import com.fyp.privacyguard.core.SharedPrefsHelper
import com.fyp.privacyguard.data.model.LoggedInUser
import com.fyp.privacyguard.login.ui.models.LoggedInUserView
import com.fyp.privacyguard.login.ui.models.LoginFormState
import com.fyp.privacyguard.login.ui.models.UserResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginViewModel(private val app: Application, private val repository: Repository) : AndroidViewModel(app) {

    private val _loginForm = MutableLiveData<LoginFormState>(LoginFormState())
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<UserResult<LoggedInUserView>>()
    val loginResult: LiveData<UserResult<LoggedInUserView>> = _loginResult

    fun login(email: String, password: String) {
        // can be launched in a separate asynchronous job
        repository.login(email, password) { result ->
            if (result is Result.Success) {
                // Save data to cache
                val user = result.data
                SharedPrefsHelper.saveUser(app.applicationContext, user)
                _loginResult.value =
                    UserResult(
                        success = LoggedInUserView(result.data.name!!)
                    )
            } else if(result is Result.Error) {
                _loginResult.value = UserResult(error = result.exception)
            }
        }
    }

    fun forgetPassword(email: String): LiveData<UserResult<Boolean>> {
        val forgetResult = MutableLiveData<UserResult<Boolean>>()
        repository.forgetPassword(email) { result ->
            if (result is Result.Success) {
                forgetResult.value = UserResult(success = true)
            } else if(result is Result.Error) {
                forgetResult.value = UserResult(error = result.exception)
            }
        }
        return forgetResult
    }

    fun loginDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            _loginForm.value =
                LoginFormState(emailError = R.string.invalid_email, isEligibleForgetPassword = false)
        } else if (!isPasswordValid(password)) {
            _loginForm.value =
                LoginFormState(passwordError = R.string.invalid_password, isEligibleForgetPassword = true)
        } else {
            _loginForm.value =
                LoginFormState(isDataValid = true, isEligibleForgetPassword = true)
        }
    }

    // A placeholder username validation check
    private fun isEmailValid(email: String): Boolean {
        return if (email.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}