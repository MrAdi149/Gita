package com.example.geeta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geeta.R
import com.example.geeta.data.Translation

class TranslationAdapter(private val translations: List<Translation>, private var textSize: Int) :
    RecyclerView.Adapter<TranslationAdapter.TranslationViewHolder>() {

    inner class TranslationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorNameTextView: TextView = itemView.findViewById(R.id.authorNameTextView)
        val translationTextView: TextView = itemView.findViewById(R.id.tversedescriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.translation_cardview_item, parent, false)
        return TranslationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
        val translation = translations[position]

        holder.authorNameTextView.text = translation.authorName
        holder.translationTextView.text = translation.description
        holder.authorNameTextView.textSize = textSize.toFloat()
        holder.translationTextView.textSize = textSize.toFloat()
    }


    override fun getItemCount(): Int {
        return translations.size
    }

    fun updateTextSize(newSize: Int) {
        textSize = newSize
        notifyDataSetChanged()
    }

    fun getAllTranslationText(): String {
        var allTranslationText = ""
        for (translation in translations) {
            allTranslationText += translation.description + "\n\n"
        }
        return allTranslationText
    }
}