package com.fyp.privacyguard.core

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.fyp.privacyguard.App
import com.fyp.privacyguard.R
import com.fyp.privacyguard.core.home.HomeFragment
import com.fyp.privacyguard.core.settings.SettingsFragment
import com.fyp.privacyguard.databinding.ActivityMainBinding
import com.fyp.privacyguard.receiver.AdminReceiver
import com.fyp.privacyguard.toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val RC_CAMERA = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val vmFactory = (application as App).vmRepositoryFactory
        val activityViewModel = ViewModelProvider(this, vmFactory).get(MainActivityViewModel::class.java)
        activityViewModel.loadUserDetails()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment

        navHostFragment.navController.apply {
            graph = createGraph(nav_graph.id, nav_graph.dest.home) {
//                fragment<HomeFragment>(nav_graph.dest.home) {
//                    label = "Settings"
//                    action(nav_graph.action.to_settings) {
//                        destinationId = nav_graph.dest.settings
//                    }
//                }
                fragment<HomeFragment>(nav_graph.dest.home) {
                    label = "Home"
//                    action(nav_graph.action.to_home) {
//                        destinationId = nav_graph.dest.home
//                    }
//                    action(nav_graph.action.to_settings) {
//                        destinationId = nav_graph.dest.settings
//                    }
                }
                fragment<SettingsFragment>(nav_graph.dest.settings) {
                    label = "Settings"
                }
            }
        }

        navHostFragment.navController.navigate(nav_graph.dest.home)

        setContentView(binding.root)

        val mDPM = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val mAdmin = ComponentName(this, AdminReceiver::class.java)

        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdmin)
            putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Press activate to grant permission")
        }
        startActivityForResult(intent, 101)


//        if(mDPM.isAdminActive(mAdmin)) {
//            toast("Admins is active")
//        } else {
//            toast("Admin is inactive")
//
//        }

        if (cameraPermissionGranted()) {
//            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, RC_CAMERA)
        }
    }

    private fun cameraPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 101) {
            if(resultCode == RESULT_OK) {
                toast("Permission granted")
            } else {
                toast("Permission failed")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_CAMERA) {
            if (cameraPermissionGranted()) {
                toast("Permission granted for camera")
            } else {
                toast("Permission not granted by user")
            }
        }
    }
}