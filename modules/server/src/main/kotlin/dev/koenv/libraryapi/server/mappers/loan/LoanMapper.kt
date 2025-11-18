package dev.koenv.libraryapi.server.mappers.loan

import dev.koenv.libraryapi.server.domain.entity.Loan
import dev.koenv.libraryapi.server.dto.loan.LoanDto

fun Loan.toDto(): LoanDto = LoanDto(
    id = requireNotNull(id),
    userId = userId,
    bookId = bookId,
    borrowedAt = borrowedAt,
    returnedAt = returnedAt
)
