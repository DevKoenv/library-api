package dev.koenv.libraryapi.dto.loan

import kotlinx.serialization.Serializable

@Serializable
data class ReturnLoanDto(
    val confirm: Boolean = true
)
