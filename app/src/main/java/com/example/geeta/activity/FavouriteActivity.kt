package com.example.geeta.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geeta.adapter.FavouriteVerseAdapter
import com.example.geeta.data.FavouriteVerse
import com.example.geeta.databinding.ActivityFavouriteBinding
import com.example.geeta.utils.Themes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class FavouriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavouriteVerseAdapter
    private val favoriteList = mutableListOf<FavouriteVerse>()
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private var currentTextSize: Int = 16 // Default text size

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Themes.loadTheme(this)

        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.favoritesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE)

        val sharedPrefTextSize = getSharedPreferences("text_size_prefs", Context.MODE_PRIVATE)
        currentTextSize = sharedPrefTextSize.getInt("text_size", 16) // Get the saved text size

        // Load the list of favorite items
        loadFavoriteList()

        // Initialize and set the adapter
        adapter = FavouriteVerseAdapter(favoriteList)
        recyclerView.adapter = adapter

        // Set up a click listener for the delete button in the adapter
        adapter.setOnDeleteClickListener { position ->
            deleteFavorite(position)
        }

        adapter.setOnExpandClickListener { position ->
            toggleItemExpansion(position)
        }
    }

    private fun loadFavoriteList() {
        val favoritesJson = sharedPreferences.getString("favoriteList", "[]")
        val favoriteListType = object : TypeToken<List<FavouriteVerse>>() {}.type
        favoriteList.clear()
        favoriteList.addAll(gson.fromJson(favoritesJson, favoriteListType))

        if (favoriteList.isEmpty()) {
            // If the list is empty, show the emptyTextView
            binding.emptyTextView.visibility = View.VISIBLE
        } else {
            // If the list is not empty, hide the emptyTextView
            binding.emptyTextView.visibility = View.GONE
        }
    }

    private fun saveFavoriteList() {
        val favoritesJson = gson.toJson(favoriteList)
        sharedPreferences.edit().putString("favoriteList", favoritesJson).apply()
    }

    private fun deleteFavorite(position: Int) {
        if (position in 0 until favoriteList.size) {
            favoriteList.removeAt(position)
            adapter.notifyItemRemoved(position)
            saveFavoriteList()
        }
    }

    private fun toggleItemExpansion(position: Int) {
        if (position in 0 until favoriteList.size) {
            val item = favoriteList[position]
            item.isExpanded = !item.isExpanded
            adapter.notifyItemChanged(position)
        }
    }
}