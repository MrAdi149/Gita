package com.example.geeta.data

data class FavouriteVerse(
    val chapterId: Int,
    val verseTitle: String,
    val verseContent: String,
    val transliteration: String,
    val wordMeanings: String,
    val translationData: String,
    val commentaryData: String,
    var isExpanded: Boolean = false
)