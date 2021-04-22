package com.ts.alex.bestforecast.ui.forecast.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Nullable
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ts.alex.bestforecast.R
import com.ts.alex.bestforecast.databinding.FragmentSettingsBinding
import com.ts.alex.bestforecast.device.job.SyncJob
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_settings,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(requireView())
        binding.vTime.text = getString(R.string.settings_time,"00","00")
        setUpSettings()
        onBackPress()
    }


    private fun setUpSettings() {
        binding.vUpdate.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.vTime.isVisible = true
                binding.vNumber.isVisible = true
                SyncJob.cancelJob()

            }else{
                binding.vTime.isVisible = false
                binding.vNumber.isVisible = false
                val text = getString(R.string.settings_cancel_update)
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            }
            binding.vSaveData.isEnabled = isChecked
        }
        binding.nHour.apply {
            maxValue = 24
            minValue = 0
            value = 0
            setOnValueChangedListener { _, _, newVal ->
                val vMinute = binding.nMinute.value
                val minute = if(vMinute < 10 ) "0$vMinute" else vMinute.toString()
                val hour = if(newVal < 10)"0$newVal" else newVal
                binding.vTime.text = getString(R.string.settings_time,hour,minute)
            }
        }
        binding.nMinute.apply {
            maxValue = 60
            minValue = 0
            value = 0
            setOnValueChangedListener{_, _, newVal ->
                val vHour = binding.nHour.value
                val hour = if(vHour < 10 ) "0$vHour" else vHour.toString()
                val minute = if(newVal < 10)"0$newVal" else newVal
                binding.vTime.text = getString(R.string.settings_time,hour,minute)
            }
        }
        binding.vSaveData.setOnClickListener{
            val hour = binding.nHour.value
            val minutes = binding.nMinute.value
            try {
                SyncJob.startJob(hour * 60L + minutes)
            }catch (e: Exception){
                Toast.makeText(requireContext(), getString(R.string.settings_zero), Toast.LENGTH_SHORT).show()
            }
            val text = getString(R.string.settings_update_data, hour.toString(), minutes.toString())
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        }
        binding.vChooseCity.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragmentToCityFragment()
            navController.navigate(action)
        }
    }


    private fun onBackPress() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = SettingsFragmentDirections.actionSettingsFragmentToForecastMainFragment()
                navController.navigate(action)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
    }

}