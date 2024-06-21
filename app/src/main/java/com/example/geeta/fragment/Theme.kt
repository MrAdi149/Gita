package com.example.geeta.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import com.example.geeta.R
import com.example.geeta.databinding.FragmentThemeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Theme : DialogFragment() {
    private lateinit var binding: FragmentThemeBinding
    private var themeMode: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentThemeBinding.inflate(LayoutInflater.from(requireContext()))
        val view = binding.root

        val sharedPreferences =
            requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val sharedPrefEditor = sharedPreferences.edit()

        // Initialize the checked state based on shared preferences
        themeMode = sharedPreferences.getInt("themeMode", 0)
        when (themeMode) {
            0 -> binding.lightThemeButton.isChecked = true
            1 -> binding.darkThemeButton.isChecked = true
            2 -> binding.blackThemeButton.isChecked = true
        }

        if (binding.themeRadioGroup.checkedRadioButtonId == View.NO_ID) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choose Theme")
            .setView(view)
            .setPositiveButton("OK") { _, _ ->
                if (binding.lightThemeButton.isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPrefEditor.putInt("themeMode", 0)
                } else if (binding.darkThemeButton.isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPrefEditor.putInt("themeMode", 1)
                } else if (binding.blackThemeButton.isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    requireActivity().setTheme(R.style.AppTheme_Black)
                    sharedPrefEditor.putInt("themeMode", 2)
                }
                sharedPrefEditor.commit()
                dismiss()
                (requireActivity()).recreate()
            }
            .setNegativeButton("Cancel") { _, _ -> dismiss() }
            .create()
    }
}