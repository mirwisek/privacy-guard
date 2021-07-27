package com.fyp.privacyguard.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fyp.privacyguard.R

class LogoFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_logo, container, false)

        val logo = view.findViewById<ImageView>(R.id.logo)

        Glide.with(requireContext()).load(R.drawable.logo)
            .circleCrop()
            .into(logo)

        return view
    }
}