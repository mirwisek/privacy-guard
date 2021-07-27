package com.fyp.privacyguard.login.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fyp.privacyguard.App
import com.fyp.privacyguard.core.MainActivity
import com.fyp.privacyguard.databinding.ActivityFullscreenBinding
import com.fyp.privacyguard.login.ui.models.Pages
import com.fyp.privacyguard.login.ui.views.PagerAdapter
import com.fyp.privacyguard.login.viewmodel.FullScreenViewModel
import com.fyp.privacyguard.switchActivity


class FullscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding
    private lateinit var vmFullScreen: FullScreenViewModel


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vmFactory = (application as App).vmRepositoryFactory
        vmFullScreen = ViewModelProvider(this, vmFactory).get(FullScreenViewModel::class.java)
        // Load current user
        vmFullScreen.loadUserDetails()

        binding = ActivityFullscreenBinding.inflate(layoutInflater)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = listOf(LogoFragment(), LoginFragment(), RegisterFragment())
        val pagerAdapter = PagerAdapter(this, fragments, supportFragmentManager)
        binding.viewPager.apply {
            setPagingEnabled(false)
            adapter = pagerAdapter
        }

        vmFullScreen.progressVisibility.observe(this, Observer { isVisible ->
            binding.loading.visibility = if(isVisible) View.VISIBLE else View.GONE
        })


        Handler(Looper.myLooper()!!).postDelayed({
            shouldSkipLogin()
            if(vmFullScreen.loggedUser.value == null) {
                moveToPage(Pages.LOGIN)
            }
        }, 1000L)

        setContentView(binding.root)
    }

    private fun shouldSkipLogin() {
        vmFullScreen.loggedUser.value?.let {
            switchActivity(MainActivity::class.java)
        }
    }

    fun moveToPage(page: Pages) {
        binding.viewPager.setCurrentItem(page.ordinal, true)
    }
}