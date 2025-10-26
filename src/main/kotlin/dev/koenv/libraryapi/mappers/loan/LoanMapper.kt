package dev.koenv.libraryapi.mappers.loan

import dev.koenv.libraryapi.domain.entity.Loan
import dev.koenv.libraryapi.dto.loan.LoanDto

fun Loan.toDto(): LoanDto = LoanDto(
    id = requireNotNull(id),
    userId = userId,
    bookId = bookId,
    borrowedAt = borrowedAt,
    returnedAt = returnedAt
)
