package com.example.geeta.data

data class Verse(
    val chapter_id: Int,
    var chapter_number: Int,
    val externalId: Int,
    val id: Int,
    var text: String,
    val title: String,
    val verse_number: Int,
    val verse_id: Int,
    val transliteration: String,
    val word_meanings: String

)