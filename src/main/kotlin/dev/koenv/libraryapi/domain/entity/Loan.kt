package dev.koenv.libraryapi.domain.entity

import dev.koenv.libraryapi.shared.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime
import java.util.*

@Serializable
data class Loan(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val bookId: UUID,
    val borrowedAt: LocalDateTime? = null,
    val returnedAt: LocalDateTime? = null
)
