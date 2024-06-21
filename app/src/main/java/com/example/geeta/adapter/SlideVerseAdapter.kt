package com.example.geeta.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geeta.R
import com.example.geeta.activity.VerseDetailActivity
import com.example.geeta.data.Verse

class SlideVerseAdapter(private val verses: List<Verse>) :
    RecyclerView.Adapter<SlideVerseAdapter.SliderVerseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderVerseViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate( R.layout.slider_verse_cardview, parent, false)
        return SliderVerseViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderVerseViewHolder, position: Int) {
        val verse = verses[position]
        holder.bind(verse)
    }

    override fun getItemCount(): Int = verses.size

    inner class SliderVerseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val verseTextView: TextView = itemView.findViewById(R.id.verseTextView)

        fun bind(verse: Verse) {
            verseTextView.text = verse.text

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, VerseDetailActivity::class.java).apply {
                    putExtra("chapter_number", verse.chapter_number)
                    putExtra("verse_title", verse.title)
                    putExtra("verse_text", verse.text)
                    putExtra("verse_transliteration", verse.transliteration)
                    putExtra("verse_word_meanings", verse.word_meanings)
                    // Add other verse details here if needed
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}