package com.fyp.privacyguard.core.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fyp.privacyguard.App
import com.fyp.privacyguard.R
import com.fyp.privacyguard.core.MainActivity
import com.fyp.privacyguard.core.MainActivityViewModel
import com.fyp.privacyguard.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var binding: FragmentSettingsBinding? = null
    private lateinit var activityViewModel: MainActivityViewModel

    private lateinit var activity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity = requireActivity() as MainActivity

        val vmFactory = (activity.application as App).vmRepositoryFactory
        activityViewModel = ViewModelProvider(activity, vmFactory).get(MainActivityViewModel::class.java)

        binding = FragmentSettingsBinding.bind(view)

        binding?.let { bind ->
            activityViewModel.loggedUser.observe(viewLifecycleOwner, Observer { loggedUser ->
                loggedUser?.let { user ->
                    bind.textEmail.text = user.email
                    bind.textName.text = user.name
                    bind.textPhone.text = user.phone
                }
            })
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}