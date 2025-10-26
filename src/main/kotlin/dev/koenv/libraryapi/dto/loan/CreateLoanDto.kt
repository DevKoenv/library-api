package dev.koenv.libraryapi.dto.loan

import dev.koenv.libraryapi.shared.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CreateLoanDto(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val bookId: UUID
)
