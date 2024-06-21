package com.example.geeta.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeta.activity.ChapterDetailsActivity
import com.example.geeta.data.Chapter
import com.example.geeta.databinding.ChapterCardviewItemBinding


class ChapterAdapter(private val chapters: List<Chapter>, private var textSize: Int) :
    RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>() {

    inner class ChapterViewHolder(private val binding: ChapterCardviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentPosition: Int = -1

        init {
            binding.root.setOnClickListener {
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val chapter = chapters[currentPosition]
                    val intent = Intent(binding.root.context, ChapterDetailsActivity::class.java)
                    intent.apply {
                        putExtra("chapter_number", chapter.chapter_number)
                        putExtra("chapter_name", chapter.name)
                        putExtra("name_meaning", chapter.name_meaning)
                        putExtra("chapter_summary", chapter.chapter_summary)
                        putExtra("chapter_summary_hindi", chapter.chapter_summary_hindi)
                        putExtra("verses_count", chapter.verses_count)
                    }
                    binding.root.context.startActivity(intent)
                }
            }
        }

        fun bind(chapter: Chapter, position: Int) {
            currentPosition = position
            binding.chapterT.text = "Chapter"
            binding.chapterNumberTextView.text = chapter.chapter_number.toString()
            binding.chapterNameTextView.text = chapter.name
            binding.chapterNameMeaningTextView.text = chapter.name_meaning
            binding.verseCount.text = chapter.verses_count.toString()

            // Set the text size for TextViews
            binding.chapterT.textSize = textSize.toFloat()
            binding.chapterNumberTextView.textSize = textSize.toFloat()
            binding.chapterNameTextView.textSize = textSize.toFloat()
            binding.chapterNameMeaningTextView.textSize = textSize.toFloat()
            binding.verseCount.textSize = textSize.toFloat()

            val totalVerses = chapter.verses_count

            // Calculate the number of read verses
            val sharedPreferences = binding.root.context.getSharedPreferences("read_verses", Context.MODE_PRIVATE)
            val readVerses = sharedPreferences.all.keys.count {
                it.endsWith("-chapter") && sharedPreferences.getInt(it, 0) == chapter.chapter_number && sharedPreferences.getBoolean(it.removeSuffix("-chapter"), false)            }

            val progress = (readVerses.toDouble() / totalVerses.toDouble()) * 100

            binding.progressBarReadCount.progress = progress.toInt()
            binding.progressTextView.text = String.format("%.2f%%", progress)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val binding = ChapterCardviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChapterViewHolder(binding)
    }



    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.bind(chapters[position], position)
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    fun updateTextSize(newSize: Int) {
        textSize = newSize
        notifyDataSetChanged()
    }

    fun updateProgressData() {
        notifyDataSetChanged()
    }
}