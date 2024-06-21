package com.example.geeta.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeta.adapter.TranslationAdapter
import com.example.geeta.data.Translation
import com.example.geeta.databinding.ActivityVerseTranslationBinding
import com.example.geeta.utils.Themes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException


class VerseTranslationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerseTranslationBinding
    private var currentTextSize: Int = 16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Themes.loadTheme(this)

        binding = ActivityVerseTranslationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefTextSize = getSharedPreferences("text_size_prefs", Context.MODE_PRIVATE)
        currentTextSize = sharedPrefTextSize.getInt("text_size", 16) // Get the saved text size

        // Retrieve the selected verse number from the intent
        val verseNumber = intent.getIntExtra("verse_id", 0)

        // Find the translations for the given verseNumber
        val translations = getTranslationsForVerse(verseNumber)

        // Set up the RecyclerView to display the translations in CardViews
        val adapter = TranslationAdapter(translations, 16)
        binding.translationRecyclerView.adapter = adapter
        binding.translationRecyclerView.layoutManager = LinearLayoutManager(this)

        updateAdapterTextSize(currentTextSize)
    }

    private fun getTranslationsForVerse(verseNumber: Int): List<Translation> {
        // Retrieve the list of translations from the JSON file
        val jsonString = getJsonDataFromAsset("translation.json")
        Log.d("VerseTranslationActivity","verseNumber $verseNumber -> $jsonString")
        val gson = Gson()
        val listTranslationType = object : TypeToken<List<Translation>>() {}.type
        val translations: List<Translation> = gson.fromJson(jsonString, listTranslationType)

        // Filter the list of translations to get the translations for the given verse number
        return translations.filter { it.verse_id == verseNumber }
    }

    private fun getJsonDataFromAsset(fileName: String): String? {
        return try {
            applicationContext.assets.open(fileName).bufferedReader().use {
                it.readText()
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }

    private fun updateAdapterTextSize(newSize: Int) {
        val recyclerViewT = binding.translationRecyclerView
        val adapterT = recyclerViewT.adapter as? TranslationAdapter
        adapterT?.updateTextSize(newSize)

    }
}