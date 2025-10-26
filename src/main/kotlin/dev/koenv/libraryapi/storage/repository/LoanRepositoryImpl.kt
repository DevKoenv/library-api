package dev.koenv.libraryapi.storage.repository

import dev.koenv.libraryapi.domain.entity.Loan
import dev.koenv.libraryapi.domain.repository.LoanRepository
import dev.koenv.libraryapi.plugins.dbQuery
import dev.koenv.libraryapi.storage.db.tables.LoansTable
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

class LoanRepositoryImpl : LoanRepository {

    override suspend fun findAll(): List<Loan> = dbQuery {
        LoansTable.selectAll().map(::toEntity)
    }

    override suspend fun findById(id: UUID): Loan? = dbQuery {
        LoansTable.selectAll()
            .where { LoansTable.id eq id }
            .map(::toEntity)
            .singleOrNull()
    }

    override suspend fun create(loan: Loan): Loan = dbQuery {
        val insertedId = LoansTable.insert {
            it[userId] = loan.userId
            it[bookId] = loan.bookId
            // borrowedAt has default CurrentDateTime in table
        } get LoansTable.id

        LoansTable.selectAll()
            .where { LoansTable.id eq insertedId }
            .map(::toEntity)
            .single()
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
        LoansTable.deleteWhere { LoansTable.id eq id } > 0
    }

    suspend fun markReturned(id: UUID, returnedAt: kotlinx.datetime.LocalDateTime): Boolean = dbQuery {
        LoansTable.update({ LoansTable.id eq id }) {
            it[LoansTable.returnedAt] = returnedAt
        } > 0
    }

    private fun toEntity(row: ResultRow): Loan = Loan(
        id = row[LoansTable.id],
        userId = row[LoansTable.userId],
        bookId = row[LoansTable.bookId],
        borrowedAt = row[LoansTable.borrowedAt],
        returnedAt = row[LoansTable.returnedAt]
    )
}
