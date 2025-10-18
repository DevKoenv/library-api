package dev.koenv.libraryapi.domain.entity

import dev.koenv.libraryapi.shared.serialization.UUIDSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Book(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    val title: String,
    val author: String,
    val isbn: String,
    val copiesAvailable: Int = 1,
    val createdAt: LocalDateTime? = null
)
