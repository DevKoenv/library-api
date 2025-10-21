package dev.koenv.libraryapi.dto.book

import kotlinx.serialization.Serializable

@Serializable
data class CreateBookDto(
    val title: String,
    val author: String,
    val isbn: String,
    val copiesAvailable: Int = 1
)
