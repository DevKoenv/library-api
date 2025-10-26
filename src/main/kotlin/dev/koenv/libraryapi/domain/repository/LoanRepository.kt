package dev.koenv.libraryapi.domain.repository

import dev.koenv.libraryapi.domain.entity.Loan
import java.util.UUID

interface LoanRepository {
    suspend fun findAll(): List<Loan>
    suspend fun findById(id: UUID): Loan?
    suspend fun create(loan: Loan): Loan
    suspend fun delete(id: UUID): Boolean
}
