package com.ts.alex.bestforecast.ui.registration

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.ts.alex.bestforecast.R
import com.ts.alex.bestforecast.databinding.FragmentRegistrationBinding
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment() {
    private val viewModel by viewModel<RegistrationViewModel>()
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var navController: NavController
    private val args: RegistrationFragmentArgs by navArgs()

    private lateinit var title: TextView
    private lateinit var inputName: TextInputLayout
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var vButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(requireView())
        onBackPress()
        title = binding.vTitle
        inputName = binding.vNameLayout
        inputEmail = binding.vEmail
        inputPassword = binding.vPassword
        vButton = binding.vButtonNext
        viewModel.setConfigScreen(args.isSignUp)

        if (args.isSignUp) {
            setUpSignUpScreen()
        } else {
            setUpSignInScreen()
        }
        validationFields()
        observeVm()

        vButton.setOnClickListener {
            binding.vProgressBar.visibility = View.VISIBLE
            viewModel.nextStep(args.isSignUp)
        }
    }

    private fun setUpSignUpScreen() {
        title.text = getString(R.string.reg_title_up)
    }

    private fun setUpSignInScreen() {
        title.text = getString(R.string.reg_title_in)
        inputName.isVisible = false
    }

    private fun validationFields() {
        inputName.editText!!.doAfterTextChanged {
            viewModel.validationName(it.toString())
        }
        inputEmail.editText!!.doAfterTextChanged {
            viewModel.validationEmail(it.toString())

        }
        inputPassword.editText!!.doAfterTextChanged {
            viewModel.validationPassword(it.toString())
        }

        inputName.editText!!.setOnFocusChangeListener { _, hasFocus ->
            showError(inputName, viewModel.isValidName, hasFocus)
        }
        inputEmail.editText!!.setOnFocusChangeListener { _, hasFocus ->
            showError(inputEmail, viewModel.isValidEmail, hasFocus)
        }
        inputPassword.editText!!.setOnFocusChangeListener { _, hasFocus ->
            showError(inputPassword, viewModel.isValidPassword, hasFocus)
        }
    }

    private fun showError(inputLayout: TextInputLayout, validName: Boolean, hasFocus: Boolean) {
        if (!hasFocus && !validName) {
            inputLayout.error = getString(R.string.reg_error_text)
        } else inputLayout.error = null
    }

    private fun observeVm() {
        lifecycleScope.launchWhenStarted {
            viewModel.enableButton.collect {
                vButton.isEnabled = it

            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.hideProgress.collect {
                binding.vProgressBar.visibility = View.GONE

            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.goToTheNextStep.collect { isCorrect ->
                val action =
                    RegistrationFragmentDirections.actionRegistrationFragmentToForecastMainFragment()
                if (isCorrect) {
                    navController.navigate(action)
                } else {
                    val message = getString(R.string.reg_incorrect_psw_email)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun onBackPress() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action =
                    RegistrationFragmentDirections.actionRegistrationFragmentToStartFragment()
                navController.navigate(action)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
    }
}