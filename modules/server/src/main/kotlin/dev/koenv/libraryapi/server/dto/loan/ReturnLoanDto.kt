package dev.koenv.libraryapi.server.dto.loan

import kotlinx.serialization.Serializable

@Serializable
data class ReturnLoanDto(
    val confirm: Boolean = true
)
