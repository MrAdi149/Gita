package com.example.geeta

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.geeta.activity.AboutGitaActivity
import com.example.geeta.activity.AllVerseActivity
import com.example.geeta.activity.FavouriteActivity
import com.example.geeta.activity.HanumanChalisaActivity
import com.example.geeta.adapter.ChapterAdapter
import com.example.geeta.adapter.SlideVerseAdapter
import com.example.geeta.data.Chapter
import com.example.geeta.data.Verse
import com.example.geeta.databinding.ActivityMainBinding
import com.example.geeta.fragment.AboutAppFragment
import com.example.geeta.fragment.Theme
import com.example.geeta.utils.Themes
import com.google.android.material.color.DynamicColors
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.io.IOException
import java.nio.charset.Charset
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var chapterList: List<Chapter>
    private lateinit var verseList: List<Verse>
    private lateinit var viewPager: ViewPager2
    private var currentTextSize: Int = 16 // Default text size

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Themes.loadTheme(this)

        val sharedPrefTextSize = getSharedPreferences("text_size_prefs", Context.MODE_PRIVATE)
        currentTextSize = sharedPrefTextSize.getInt("text_size", 16) // Get the saved text size

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DynamicColors.applyToActivityIfAvailable(this)

        verseList = loadVersesFromJson()
        verseList = verseList.shuffled(Random(System.currentTimeMillis()))

        // Load JSON data from assets
        val jsonString = applicationContext.assets.open("chapters.json").bufferedReader().use {
            it.readText()
        }

        // Parse JSON data
        chapterList = parseJson(jsonString)

        val adapterC = ChapterAdapter(chapterList, 16)
        binding.recyclerView.adapter = adapterC
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        updateAdapterTextSize(currentTextSize)
        // Setup the toolbar
        setSupportActionBar(binding.toolbar)

        // Setup ViewPager
        viewPager = binding.viewPager
        val adapter = SlideVerseAdapter(verseList)
        binding.viewPager.adapter = adapter

        // Auto slide after every 10 seconds
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                binding.viewPager.currentItem = (binding.viewPager.currentItem + 1) % verseList.size
                handler.postDelayed(this, 10000)
            }
        }
        handler.postDelayed(runnable, 10000)

        val progressValue = when (currentTextSize) {
            16 -> 0
            20 -> 1
            24 -> 2
            28 -> 3
            32 -> 4
            else -> 1 // Default text size
        }

        binding.textSizeSeekBar.progress = progressValue

        val textSizeSeekBar = binding.textSizeSeekBar
        textSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update the text size when the SeekBar progress changes
                val newSize = when (progress) {
                    0 -> 16
                    1 -> 20
                    2 -> 24
                    3 -> 28
                    4 -> 32
                    else -> 16 // Default text size
                }

                updateAdapterTextSize(newSize)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.hanumanChalisaText.setOnClickListener {
            val intent = Intent(this, HanumanChalisaActivity::class.java)
            startActivity(intent)
        }

        binding.btnAllVerse.setOnClickListener {
            val intent = Intent(this, AllVerseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val adapterC = binding.recyclerView.adapter as? ChapterAdapter
        adapterC?.updateProgressData()
        adapterC?.notifyDataSetChanged()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_about_gita -> {
                intent.setClass(this, AboutGitaActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_hanuman_chalisa -> {
                intent.setClass(this, HanumanChalisaActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_theme -> {
                val themeDialog = Theme()
                themeDialog.show(supportFragmentManager, "theme_dialog")
                return true
            }
            R.id.nav_about -> {
                val aboutDialog = AboutAppFragment()
                aboutDialog.show(supportFragmentManager, "AboutAppFragment")

            }
            R.id.nav_fav -> {
                intent.setClass(this, FavouriteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun updateAdapterTextSize(newSize: Int) {

        val recyclerViewC = binding.recyclerView
        val adapterC = recyclerViewC.adapter as? ChapterAdapter
        adapterC?.updateTextSize(newSize)

        val sharedPrefTextSize= getSharedPreferences("text_size_prefs", Context.MODE_PRIVATE)
        sharedPrefTextSize.edit().putInt("text_size", newSize).apply()
    }
    private fun loadVersesFromJson(): List<Verse> {
        val json: String?
        try {
            val inputStream = assets.open("verse.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.defaultCharset())
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }
        val listType = object : TypeToken<List<Verse>>() {}.type
        return Gson().fromJson(json, listType)
    }
    private fun parseJson(jsonString: String): List<Chapter> {
        val chapterList = mutableListOf<Chapter>()
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val chapterJson = jsonArray.getJSONObject(i)
            val chapter = Chapter(
                chapterJson.getInt("chapter_number"),
                chapterJson.getString("chapter_summary"),
                chapterJson.getString("chapter_summary_hindi"),
                chapterJson.getInt("id"),
                chapterJson.getString("image_name"),
                chapterJson.getString("name"),
                chapterJson.getString("name_meaning"),
                chapterJson.getString("name_translation"),
                chapterJson.getString("name_transliterated"),
                chapterJson.getInt("verses_count")
            )
            chapterList.add(chapter)
        }
        return chapterList
    }
}