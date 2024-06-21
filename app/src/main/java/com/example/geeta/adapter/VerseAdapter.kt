package com.example.geeta.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.geeta.R
import com.example.geeta.activity.VerseDetailActivity
import com.example.geeta.data.Verse
import com.example.geeta.databinding.VerseCardviewItemBinding

class VerseAdapter(private val verses: List<Verse>, private var textSize: Int) :
    RecyclerView.Adapter<VerseAdapter.VerseViewHolder>() {

    inner class VerseViewHolder(private val binding: VerseCardviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(verse: Verse) {
            binding.verseTitleTextView.text = verse.title
            binding.verseTitleTextView.textSize = textSize.toFloat()

            val sharedPreferences = binding.root.context.getSharedPreferences("read_verses", Context.MODE_PRIVATE)
            val verseId = verse.verse_id
            val isVerseRead = sharedPreferences.getBoolean("$verseId", false)

            if (isVerseRead) {
                binding.cardviewVerseItem.strokeColor = ContextCompat.getColor(binding.root.context, R.color.md_theme_light_primary)
            } else {
                //do nothing
            }

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, VerseDetailActivity::class.java)
                intent.putExtra("chapter_number", verse.chapter_number)
                intent.putExtra("verse_title", verse.title)
                intent.putExtra("verse_text", verse.text)
                intent.putExtra("verse_transliteration", verse.transliteration)
                intent.putExtra("verse_word_meanings", verse.word_meanings)
                // Add other verse details here if needed
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerseViewHolder {
        val binding = VerseCardviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VerseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VerseViewHolder, position: Int) {
        holder.bind(verses[position])
    }

    override fun getItemCount(): Int {
        return verses.size
    }

    fun updateTextSize(newSize: Int) {
        textSize = newSize
        notifyDataSetChanged()
    }

    fun updateProgressData() {
        notifyDataSetChanged()
    }
}