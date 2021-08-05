package com.fyp.privacyguard.login.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fyp.privacyguard.App
import com.fyp.privacyguard.core.MainActivity
import com.fyp.privacyguard.databinding.ActivityFullscreenBinding
import com.fyp.privacyguard.log.FileLogManager
import com.fyp.privacyguard.login.ui.models.Pages
import com.fyp.privacyguard.login.ui.views.PagerAdapter
import com.fyp.privacyguard.login.viewmodel.FullScreenViewModel
import com.fyp.privacyguard.switchActivity
import java.util.ArrayList


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

        setContentView(binding.root)

        if (checkPermissions().isEmpty()) {
            delayEnter()
        } else {
            requestPermissions()
        }
    }

    private fun shouldSkipLogin() {
        vmFullScreen.loggedUser.value?.let {
            switchActivity(MainActivity::class.java)
        }
    }

    fun moveToPage(page: Pages) {
        binding.viewPager.setCurrentItem(page.ordinal, true)
    }


    private fun checkPermissions(): List<String> {
        val permissions: MutableList<String> = ArrayList()
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_PHONE_STATE)
        }
        return permissions
    }

    private fun requestPermissions() {
        val permissions = checkPermissions()
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (i in permissions.indices) {
            val permission = permissions[i]
            if (permission == Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                FileLogManager.init()
                break
            }
        }
        delayEnter()
    }

    private fun delayEnter() {
        object : CountDownTimer(1200, 1200) {
            override fun onTick(l: Long) {}
            override fun onFinish() {
                shouldSkipLogin()
                if(vmFullScreen.loggedUser.value == null) {
                    moveToPage(Pages.LOGIN)
                }
            }
        }.start()
    }
}