package com.fyp.privacyguard

import android.app.Application
import com.fyp.privacyguard.data.Repository
import com.fyp.privacyguard.shared.RepositoryViewModelFactory

class App: Application() {

    lateinit var vmRepositoryFactory: RepositoryViewModelFactory private set
    lateinit var repository: Repository private set

    override fun onCreate() {
        super.onCreate()
        repository = Repository()
        vmRepositoryFactory = RepositoryViewModelFactory(this, repository)
    }
}