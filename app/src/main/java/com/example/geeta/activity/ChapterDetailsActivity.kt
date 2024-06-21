package com.example.geeta.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeta.adapter.VerseAdapter
import com.example.geeta.data.Verse
import com.example.geeta.databinding.ActivityChapterDetailsBinding
import com.example.geeta.utils.Themes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ChapterDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChapterDetailsBinding
    private var verseList: List<Verse> = emptyList()
    private var isSummaryExpanded = false
    private var isSummaryHindiExpanded = false
    private lateinit var progressBar: ProgressBar
    private var currentTextSize: Int = 16

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Themes.loadTheme(this)

        binding = ActivityChapterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefTextSize = getSharedPreferences("text_size_prefs", Context.MODE_PRIVATE)
        currentTextSize = sharedPrefTextSize.getInt("text_size", 16) // Get the saved text size

        progressBar = binding.progressBar
        updateAdapterTextSize(currentTextSize)
        updateTextSize(currentTextSize)
        setSupportActionBar(binding.toolbar)

        // Retrieve the chapter details from the intent
        val chapterNumber = intent.getIntExtra("chapter_number", 0)
        val chapterName = intent.getStringExtra("chapter_name")
        val chapterNameMeaning = intent.getStringExtra("name_meaning")
        val chapterSummary = intent.getStringExtra("chapter_summary")
        val chapterSummaryHindi = intent.getStringExtra("chapter_summary_hindi")
        val versesCount = intent.getIntExtra("verses_count", 0)

        progressBar.visibility = View.VISIBLE

        val verse = loadVersesForChapter(chapterNumber)
        val adapter = VerseAdapter(verse, currentTextSize)
        binding.verseRecyclerView.adapter = adapter
        binding.verseRecyclerView.layoutManager = LinearLayoutManager(this)

        // Load the verses asynchronously
        GlobalScope.launch(Dispatchers.IO) {
            verseList = loadVersesForChapter(chapterNumber)

            // Update the UI on the main thread
            withContext(Dispatchers.Main) {
                // Set the chapter details in the UI
                binding.chapterNameTextView.text = chapterName
                binding.chapterNameMeaningTextView.text = chapterNameMeaning
                binding.verseRecyclerView.layoutManager = LinearLayoutManager(this@ChapterDetailsActivity)
                binding.verseRecyclerView.adapter = VerseAdapter(verseList, currentTextSize)

                supportActionBar?.title = "Chapter $chapterNumber"

                // Hide the ProgressBar once the verses are loaded
                progressBar.visibility = View.GONE
            }
        }

        // Show only two lines of the English version of the chapter summary initially
        binding.chapterSummaryTextView.text = getEllipsizedText(chapterSummary ?: "", 2, 40)
        binding.seeMoreTextView.setOnClickListener {
            isSummaryExpanded = !isSummaryExpanded
            if (isSummaryExpanded) {
                binding.chapterSummaryTextView.text = chapterSummary
                binding.seeMoreTextView.text = "See Less"
            } else {
                binding.chapterSummaryTextView.text = getEllipsizedText(chapterSummary ?: "", 2, 40)
                binding.seeMoreTextView.text = "See More"
            }
        }

        // Show only two lines of the Hindi version of the chapter summary initially
        binding.chapterSummaryHindiTextView.text = getEllipsizedText(chapterSummaryHindi ?: "", 2, 40)
        binding.seeMoreHindiTextView.setOnClickListener {
            isSummaryHindiExpanded = !isSummaryHindiExpanded
            if (isSummaryHindiExpanded) {
                binding.chapterSummaryHindiTextView.text = chapterSummaryHindi
                binding.seeMoreHindiTextView.text = "छोटा करें"
            } else {
                binding.chapterSummaryHindiTextView.text = getEllipsizedText(chapterSummaryHindi ?: "", 2, 40)
                binding.seeMoreHindiTextView.text = "और देखें"
            }
        }

        binding.verseRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.verseRecyclerView.adapter = VerseAdapter(verseList, 16)

        // Calculate the number of read verses
        val sharedPreferences = binding.root.context.getSharedPreferences("read_verses", Context.MODE_PRIVATE)
        val readVerses = sharedPreferences.all.keys.count {
            it.endsWith("-chapter") && sharedPreferences.getInt(it, 0) == chapterNumber && sharedPreferences.getBoolean(it.removeSuffix("-chapter"), false)            }

        val progress = (readVerses.toDouble() / versesCount.toDouble()) * 100

        binding.progressBarReadCount.progress = progress.toInt()
        binding.progressTextView.text = String.format("%.2f%%", progress)
    }

    override fun onResume() {
        super.onResume()
        val adapter = binding.verseRecyclerView.adapter as? VerseAdapter
        adapter?.updateProgressData()
        adapter?.notifyDataSetChanged()
    }

    private fun getEllipsizedText(text: String, maxLines: Int, maxCharactersPerLine: Int): String {
        val maxCharacters = maxLines * maxCharactersPerLine
        return if (text.length > maxCharacters) {
            "${text.substring(0, maxCharacters)}..."
        } else {
            text
        }
    }

    private fun loadJsonFromAsset(fileName: String): String? {
        return try {
            applicationContext.assets.open(fileName).bufferedReader().use {
                it.readText()
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }

    private fun loadVersesForChapter(chapterNumber: Int): List<Verse> {
        val jsonString = loadJsonFromAsset("verse.json")
        val verseListType = object : TypeToken<List<Verse>>() {}.type

        val allVerses: List<Verse> = Gson().fromJson(jsonString, verseListType)

        return allVerses.filter { it.chapter_number == chapterNumber }
    }

    private fun updateTextSize(newSize: Int) {

        currentTextSize = newSize
        val textViewList = listOf(
            binding.chapterNameTextView,
            binding.chapterNameMeaningTextView,
            binding.chapterSummaryTextView,
            binding.chapterSummaryHindiTextView,
            binding.seeMoreTextView,
            binding.seeMoreHindiTextView
        )

        textViewList.forEach { textView ->
            textView.textSize = newSize.toFloat()
        }
    }

    private fun updateAdapterTextSize(newSize: Int) {

        val recyclerViewC = binding.verseRecyclerView
        val adapterC = recyclerViewC.adapter as? VerseAdapter
        adapterC?.updateTextSize(newSize)

    }
}