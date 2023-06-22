package com.example.myapp

import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentClientMenuBinding
import com.example.myapp.databinding.FragmentFirstBinding
import com.example.myapp.databinding.FragmentSettingsBinding
import androidx.fragment.app.FragmentTransaction
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentSettings : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonDarkMode.setOnClickListener {
            setNightMode(true)
        }

        binding.buttonlightMode.setOnClickListener {
            setNightMode(false)
        }

        binding.buttonCredits.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
                .replace(R.id.settings_container, CreditsFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }



    }

    private fun setNightMode(enabled: Boolean) {
        val uiModeManager = requireContext().getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        if (enabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            uiModeManager.nightMode = UiModeManager.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            uiModeManager.nightMode = UiModeManager.MODE_NIGHT_NO
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}