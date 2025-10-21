package dev.koenv.libraryapi.dto.book

import kotlinx.serialization.Serializable

@Serializable
data class UpdateBookDto(
    val title: String? = null,
    val author: String? = null,
    val isbn: String? = null,
    val copiesAvailable: Int? = null
)
