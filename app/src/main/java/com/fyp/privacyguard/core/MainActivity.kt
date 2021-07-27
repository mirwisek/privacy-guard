package com.fyp.privacyguard.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.fyp.privacyguard.App
import com.fyp.privacyguard.R
import com.fyp.privacyguard.core.home.HomeFragment
import com.fyp.privacyguard.core.settings.SettingsFragment
import com.fyp.privacyguard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
    }
}