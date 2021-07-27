package com.fyp.privacyguard.login.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fyp.privacyguard.App
import com.fyp.privacyguard.R
import com.fyp.privacyguard.data.model.LoggedInUser
import com.fyp.privacyguard.databinding.LayoutRegisterBinding
import com.fyp.privacyguard.login.ui.models.LoggedInUserView
import com.fyp.privacyguard.login.ui.models.Pages
import com.fyp.privacyguard.login.viewmodel.FullScreenViewModel
import com.fyp.privacyguard.login.viewmodel.RegisterViewModel
import com.fyp.privacyguard.toast

class RegisterFragment : Fragment(R.layout.layout_register) {

    private var binding: LayoutRegisterBinding? = null
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var vmFullScreen: FullScreenViewModel

    private lateinit var activity: FullscreenActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity = requireActivity() as FullscreenActivity
        vmFullScreen = ViewModelProvider(activity).get(FullScreenViewModel::class.java)

        val vmFactory = (activity.application as App).vmRepositoryFactory
        registerViewModel = ViewModelProvider(this, vmFactory).get(RegisterViewModel::class.java)

        binding = LayoutRegisterBinding.bind(view)

        binding?.let { bind ->
            registerViewModel.registerFormState.observe(viewLifecycleOwner,
                Observer { registerFormState ->
                    if (registerFormState == null) {
                        return@Observer
                    }
                    registerFormState.emailError?.let {
                        bind.editTextEmail.error = getString(it)
                    }
                    registerFormState.nameError?.let {
                        bind.editTextName.error = getString(it)
                    }
                    registerFormState.phoneError?.let {
                        bind.editTextMobile.error = getString(it)
                    }
                    registerFormState.passwordError?.let {
                        bind.editTextPassword.error = getString(it)
                    }
                })

            registerViewModel.registerResult.observe(viewLifecycleOwner,
                Observer { registerResult ->
                    registerResult ?: return@Observer
                    hideProgress()
                    toggleFormInput(true)
                    registerResult.error?.let {
                        showRegistrationFailed(getString(R.string.signup_failed) + it.message)
                        it.printStackTrace()
                    }
                    registerResult.success?.let {
                        updateUiWithUser(it)
                    }
                })

            val afterTextChangedListener = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // ignore
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    // ignore
                }

                override fun afterTextChanged(s: Editable) {
                    registerViewModel.registerDataChanged(
                        LoggedInUser(
                            bind.editTextEmail.text.toString(),
                            bind.editTextName.text.toString(),
                            bind.editTextMobile.text.toString(),
                            bind.editTextPassword.text.toString()
                        )
                    )
                }
            }

            bind.editTextEmail.addTextChangedListener(afterTextChangedListener)
            bind.editTextPassword.addTextChangedListener(afterTextChangedListener)

            bind.editTextPassword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerUser()
                }
                false
            }

            bind.btnSignup.setOnClickListener {
                registerUser()
            }

            bind.login.setOnClickListener {
                moveToPage(Pages.LOGIN)
            }

        }
    }

    private fun registerUser() {
        binding?.let { bind ->
            if (registerViewModel.registerFormState.value?.isDataValid == true) {
                showProgress()
                toggleFormInput(false)
                registerViewModel.register(
                    LoggedInUser(
                        bind.editTextEmail.text.toString(),
                        bind.editTextName.text.toString(),
                        bind.editTextMobile.text.toString(),
                        bind.editTextPassword.text.toString()
                    )
                )
            } else {
                toast(getString(R.string.incorrect_fields))
            }
        }
    }

    private fun toggleFormInput(enabled: Boolean) {
        binding?.apply {
            btnSignup.isEnabled = enabled
            login.isEnabled = enabled
        }
    }

    private fun showProgress() {
        vmFullScreen.progressVisibility.value = true
    }
    private fun hideProgress() {
        vmFullScreen.progressVisibility.value = false
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = "Registration success: " + model.displayName
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
        moveToPage(Pages.LOGIN)
    }

    private fun showRegistrationFailed(errorString: String) {
        hideProgress()
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun moveToPage(page: Pages) {
        (requireActivity() as FullscreenActivity).moveToPage(page)
    }
}