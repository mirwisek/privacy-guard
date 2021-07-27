package com.fyp.privacyguard.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fyp.privacyguard.data.Repository
import com.fyp.privacyguard.data.model.LoggedInUser
import kotlinx.coroutines.launch

class MainActivityViewModel(private val app: Application, private val repository: Repository) :
    AndroidViewModel(app) {

    private val _loggedUser = MutableLiveData<LoggedInUser?>()
    val loggedUser: LiveData<LoggedInUser?> = _loggedUser

    fun loadUserDetails() {
        _loggedUser.value = repository.user
    }

    fun logout() {
        viewModelScope.launch {
            SharedPrefsHelper.deleteUser(app.applicationContext)
            repository.setLoggedInUser(null)
            _loggedUser.value = null
        }
    }

}