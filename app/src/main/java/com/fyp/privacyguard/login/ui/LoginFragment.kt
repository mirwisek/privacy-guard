package com.fyp.privacyguard.login.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fyp.privacyguard.*
import com.fyp.privacyguard.core.MainActivity
import com.fyp.privacyguard.databinding.LayoutLoginBinding
import com.fyp.privacyguard.login.ui.models.LoggedInUserView
import com.fyp.privacyguard.login.ui.models.Pages
import com.fyp.privacyguard.login.viewmodel.FullScreenViewModel
import com.fyp.privacyguard.login.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment(R.layout.layout_login) {

    private var binding: LayoutLoginBinding? = null
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var vmFullScreen: FullScreenViewModel

    private lateinit var activity: FullscreenActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity = requireActivity() as FullscreenActivity
        vmFullScreen = ViewModelProvider(activity).get(FullScreenViewModel::class.java)

        val vmFactory = (activity.application as App).vmRepositoryFactory
        loginViewModel = ViewModelProvider(this, vmFactory).get(LoginViewModel::class.java)

        binding = LayoutLoginBinding.bind(view)

        binding?.let { bind ->
            loginViewModel.loginFormState.observe(viewLifecycleOwner,
                Observer { loginFormState ->
                    if (loginFormState == null) {
                        return@Observer
                    }
                    if (loginFormState.isEligibleForgetPassword)
                        bind.forgotPassword.setTextColor(requireContext().getColorCompat(R.color.light_blue_900))
                    else
                        bind.forgotPassword.setTextColor(requireContext().getColorCompat(R.color.black))

                    loginFormState.emailError?.let {
                        bind.editTextEmail.error = getString(it)
                    }
                    loginFormState.passwordError?.let {
                        bind.editTextPassword.error = getString(it)
                    }
                })

            loginViewModel.loginResult.observe(viewLifecycleOwner,
                Observer { loginResult ->
                    loginResult ?: return@Observer
                    hideProgress()
                    toggleFormInput(true)
                    loginResult.error?.let {
                        val msg = getString(R.string.login_failed) + it.localizedMessage
                        showLoginFailed(msg)
                        it.printStackTrace()
                    }
                    loginResult.success?.let {
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
                    loginViewModel.loginDataChanged(
                        bind.editTextEmail.text.toString(),
                        bind.editTextPassword.text.toString()
                    )
                }
            }

            bind.editTextEmail.addTextChangedListener(afterTextChangedListener)
            bind.editTextPassword.addTextChangedListener(afterTextChangedListener)
            bind.editTextPassword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginUser()
                }
                false
            }

            bind.btnLogin.setOnClickListener {
                loginUser()
            }

            bind.signup.setOnClickListener {
                moveToPage(Pages.REGISTER)
            }

            bind.forgotPassword.setOnClickListener {
                forgetPassword()
            }

        }
    }

    private fun forgetPassword() {
        binding?.let { bind ->
            val loginForm = loginViewModel.loginFormState.value

            if (loginForm != null && loginForm.isEligibleForgetPassword && loginForm.emailError == null) {
                showProgress()
                toggleFormInput(false)
                val liveData = loginViewModel.forgetPassword(bind.editTextEmail.text.toString())
                liveData.observe(viewLifecycleOwner, Observer { result ->
                    result?.let {
                        if (result.success == true) {
                            bind.root.showSnackbar(
                                getString(R.string.forget_pass_sent),
                                Snackbar.LENGTH_LONG
                            )
                        } else {
                            val msg = result.error?.message ?: getString(R.string.forget_request_failed)
                            bind.root.showSnackbar(msg)
                        }
                        hideProgress()
                        toggleFormInput(true)
                        liveData.removeObservers(viewLifecycleOwner)
                    }
                })
            } else {
                bind.root.showSnackbar(getString(R.string.forget_pass_hint))
            }
        }
    }

    private fun loginUser() {
        binding?.let { bind ->
            if (loginViewModel.loginFormState.value?.isDataValid == true) {
                toggleFormInput(false)
                showProgress()
                loginViewModel.login(
                    bind.editTextEmail.text.toString(),
                    bind.editTextPassword.text.toString()
                )
            } else {
                toast(getString(R.string.incorrect_fields))
            }
        }
    }

    private fun toggleFormInput(enabled: Boolean) {
        binding?.apply {
            btnLogin.isEnabled = enabled
            forgotPassword.isEnabled = enabled
            signup.isEnabled = enabled
        }
    }

    private fun showProgress() {
        vmFullScreen.progressVisibility.value = true
    }

    private fun hideProgress() {
        vmFullScreen.progressVisibility.value = false
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
        startActivity(Intent(activity, MainActivity::class.java))
        activity.finish()
    }

    private fun showLoginFailed(errorString: String) {
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