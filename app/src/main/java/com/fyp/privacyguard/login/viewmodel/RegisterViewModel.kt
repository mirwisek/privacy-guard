package com.fyp.privacyguard.login.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import com.fyp.privacyguard.data.Repository
import com.fyp.privacyguard.data.Result

import com.fyp.privacyguard.R
import com.fyp.privacyguard.core.SharedPrefsHelper
import com.fyp.privacyguard.data.model.LoggedInUser
import com.fyp.privacyguard.login.ui.models.LoggedInUserView
import com.fyp.privacyguard.login.ui.models.UserResult
import com.fyp.privacyguard.login.ui.models.RegisterFormState
import com.fyp.privacyguard.sharedPrefs

class RegisterViewModel(private val app: Application, private val repository: Repository) :
    AndroidViewModel(app) {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<UserResult<LoggedInUserView>>()
    val registerResult: LiveData<UserResult<LoggedInUserView>> = _registerResult

    fun register(user: LoggedInUser) {
        // can be launched in a separate asynchronous job
        repository.register(user) { result ->
            if (result is Result.Success) {
                val returnedUser = result.data
                SharedPrefsHelper.saveUser(app.applicationContext, returnedUser)

                _registerResult.value =
                    UserResult(
                        success = LoggedInUserView(
                            displayName = returnedUser.name!!
                        )
                    )
            } else if(result is Result.Error) {
                _registerResult.value = UserResult(error = result.exception)
            }
        }
    }

    fun registerDataChanged(user: LoggedInUser) {
        if (!isEmailValid(user.email!!)) {
            _registerForm.value =
                RegisterFormState(emailError = R.string.invalid_email)
        } else if (!isNameValid(user.name!!)) {
            _registerForm.value =
                RegisterFormState(nameError = R.string.invalid_name)
        } else if (!isPhoneValid(user.phone!!)) {
            _registerForm.value =
                RegisterFormState(phoneError = R.string.invalid_phone)
        } else if (user.password != null && !isPasswordValid(user.password!!)) {
            _registerForm.value =
                RegisterFormState(passwordError = R.string.invalid_password)
        } else {
            _registerForm.value =
                RegisterFormState(isDataValid = true)
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

    private fun isPhoneValid(phone: String): Boolean {
        return phone.length == 11
    }

    private fun isNameValid(name: String): Boolean {
        return name.length > 2
    }
}