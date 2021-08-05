package com.fyp.privacyguard.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fyp.privacyguard.core.SharedPrefsHelper
import com.fyp.privacyguard.data.Repository
import com.fyp.privacyguard.data.model.LoggedInUser
import kotlinx.coroutines.launch

class FullScreenViewModel(private val app: Application, private val repository: Repository) :
    AndroidViewModel(app) {

    private val _loggedUser = MutableLiveData<LoggedInUser?>()
    val loggedUser: LiveData<LoggedInUser?> = _loggedUser

    val progressVisibility = MutableLiveData<Boolean>(false)

    fun loadUserDetails() {
        viewModelScope.launch {
            SharedPrefsHelper.getUser(app.applicationContext)?.let { user ->
                _loggedUser.value = user
                repository.setLoggedInUser(user)
            }
        }
    }

}