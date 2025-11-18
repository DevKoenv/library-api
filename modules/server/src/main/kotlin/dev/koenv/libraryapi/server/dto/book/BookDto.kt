package dev.koenv.libraryapi.server.dto.book

import dev.koenv.libraryapi.server.shared.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BookDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val title: String,
    val author: String,
    val isbn: String,
    val copiesAvailable: Int
)
