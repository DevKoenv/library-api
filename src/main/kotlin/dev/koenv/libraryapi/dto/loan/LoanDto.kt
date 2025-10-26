package dev.koenv.libraryapi.dto.loan

import dev.koenv.libraryapi.shared.serialization.UUIDSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class LoanDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val bookId: UUID,
    val borrowedAt: LocalDateTime?,
    val returnedAt: LocalDateTime?
)
