package com.example.geeta.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.geeta.databinding.FragmentHanumanChalisaHBinding

class HanumanChalisaH : Fragment() {
    private lateinit var binding: FragmentHanumanChalisaHBinding
    private var currentTextSize: Int = 16

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHanumanChalisaHBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load the saved text size from SharedPreferences
        val sharedPrefTextSize = requireActivity().getSharedPreferences("text_size_prefs", Context.MODE_PRIVATE)
        currentTextSize = sharedPrefTextSize.getInt("text_size", 16)

        // Set the initial text size
        updateTextSize(currentTextSize)

    }

    private fun updateTextSize(newSize: Int) {
        currentTextSize = newSize

        val textViewList = listOf(
            binding.hanumanChalisaH
        )

        textViewList.forEach { textView ->
            textView.textSize = newSize.toFloat()
        }
    }
}