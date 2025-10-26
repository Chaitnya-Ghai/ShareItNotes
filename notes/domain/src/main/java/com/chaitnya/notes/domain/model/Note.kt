package com.chaitnya.notes.domain.model

data class Note(
    val id: String = "",
    val email: String = "",
    val title: String = "",
    val content: String = "",
    val shared: Boolean = false,
    val imgUrl: String = "",
    val imgPath: String = "",
    val timeStamp: Long = System.currentTimeMillis()
)
