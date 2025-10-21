package dev.koenv.libraryapi.storage.repository

import dev.koenv.libraryapi.domain.entity.Book
import dev.koenv.libraryapi.domain.repository.BookRepository
import dev.koenv.libraryapi.plugins.dbQuery
import dev.koenv.libraryapi.storage.db.tables.BooksTable
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.*
import java.util.*

class BookRepositoryImpl : BookRepository {

    override suspend fun findAll(): List<Book> = dbQuery {
        BooksTable.selectAll().map(::toEntity)
    }

    override suspend fun findById(id: UUID): Book? = dbQuery {
        BooksTable.selectAll()
            .where { BooksTable.id eq id }
            .map(::toEntity)
            .singleOrNull()
    }

    override suspend fun existsById(id: UUID): Boolean = dbQuery {
        !BooksTable.select(BooksTable.id).where { BooksTable.id eq id }.empty()
    }

    override suspend fun findByIsbn(isbn: String): Book? = dbQuery {
        BooksTable.selectAll()
            .where { BooksTable.isbn eq isbn }
            .map(::toEntity)
            .singleOrNull()
    }

    override suspend fun create(entity: Book): Book = dbQuery {
        val insertedId = BooksTable.insert {
            it[title] = entity.title
            it[author] = entity.author
            it[isbn] = entity.isbn
            it[copiesAvailable] = entity.copiesAvailable
        } get BooksTable.id

        BooksTable.selectAll()
            .where { BooksTable.id eq insertedId }
            .map(::toEntity)
            .single()
    }

    override suspend fun update(id: UUID, entity: Book): Book? = dbQuery {
        val updated = BooksTable.update({ BooksTable.id eq id }) {
            it[title] = entity.title
            it[author] = entity.author
            it[isbn] = entity.isbn
            it[copiesAvailable] = entity.copiesAvailable
        }
        if (updated > 0)
            BooksTable.selectAll().where { BooksTable.id eq id }.map(::toEntity).singleOrNull()
        else null
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
        BooksTable.deleteWhere { BooksTable.id eq id } > 0
    }

    override suspend fun count(): Long = dbQuery {
        BooksTable.selectAll().count()
    }

    private fun toEntity(row: ResultRow): Book = Book(
        id = row[BooksTable.id],
        title = row[BooksTable.title],
        author = row[BooksTable.author],
        isbn = row[BooksTable.isbn],
        copiesAvailable = row[BooksTable.copiesAvailable],
        createdAt = row[BooksTable.createdAt]
    )
}
