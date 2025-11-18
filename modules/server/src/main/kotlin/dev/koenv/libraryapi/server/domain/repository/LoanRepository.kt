package dev.koenv.libraryapi.server.domain.repository

import dev.koenv.libraryapi.server.domain.entity.Loan
import java.util.UUID

interface LoanRepository {
    suspend fun findAll(): List<Loan>
    suspend fun findById(id: UUID): Loan?
    suspend fun create(loan: Loan): Loan
    suspend fun delete(id: UUID): Boolean
}
