package com.fyp.privacyguard.shared

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fyp.privacyguard.core.MainActivityViewModel
import com.fyp.privacyguard.data.Repository
import com.fyp.privacyguard.login.viewmodel.FullScreenViewModel
import com.fyp.privacyguard.login.viewmodel.LoginViewModel
import com.fyp.privacyguard.login.viewmodel.RegisterViewModel

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class RepositoryViewModelFactory(private val app: Application, private val repository: Repository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(app, repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(app, repository) as T
            }
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> {
                MainActivityViewModel(app, repository) as T
            }
            modelClass.isAssignableFrom(FullScreenViewModel::class.java) -> {
                FullScreenViewModel(app, repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}