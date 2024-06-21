package com.example.geeta.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geeta.databinding.ActivityAboutGitaBinding
import com.example.geeta.utils.Themes
import com.example.geeta.R

class AboutGitaActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAboutGitaBinding
    private var currentTextSize: Int = 16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Themes.loadTheme(this)

        binding = ActivityAboutGitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefTextSize = getSharedPreferences("text_size_prefs", Context.MODE_PRIVATE)
        currentTextSize = sharedPrefTextSize.getInt("text_size", 16) // Get the saved text size

        updateTextSize(currentTextSize)

    }

    private fun updateTextSize(newSize: Int) {

        currentTextSize = newSize
        val textViewList = listOf(
            binding.textViewaboutGita1,
            binding.textViewaboutGita3

        )

        textViewList.forEach { textView ->
            textView.textSize = newSize.toFloat()
        }

    }
}