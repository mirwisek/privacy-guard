package com.fyp.privacyguard.core.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fyp.privacyguard.App
import com.fyp.privacyguard.R
import com.fyp.privacyguard.core.*
import com.fyp.privacyguard.databinding.FragmentHomeBinding
import com.fyp.privacyguard.login.ui.FullscreenActivity
import com.fyp.privacyguard.toast
import com.fyp.privacyguard.switchActivity

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var binding: FragmentHomeBinding? = null

    private lateinit var activity: MainActivity
    private lateinit var vmMain: MainActivityViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity = requireActivity() as MainActivity
        val vmFactory = (activity.application as App).vmRepositoryFactory
        vmMain = ViewModelProvider(activity, vmFactory).get(MainActivityViewModel::class.java)

        binding = FragmentHomeBinding.bind(view)

        val list = listOf(
            GridEntry.CheckLogs()
            , GridEntry.ChangeInterval()
            , GridEntry.FingerRecognition()
            , GridEntry.FaceRecognition()
            , GridEntry.Settings()
            , GridEntry.Logout()
        )

        val adapter = GridAdapter(requireContext()) { entry ->
            when(entry) {
                is GridEntry.Logout -> {
                    vmMain.logout()
                    activity.switchActivity(FullscreenActivity::class.java)
                }
                is GridEntry.Settings -> {
                    findNavController().navigate(nav_graph.dest.settings)
                }
                else -> {
                    toast(entry.title)
                }
            }
        }
        adapter.updateList(list)

//        val manager = GridLayoutManager(this, 2)
        val manager = AutoFitGridLayoutManager(requireContext(), 2)

        with(binding!!.recyclerView) {
            layoutManager = manager
            this.adapter = adapter
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}