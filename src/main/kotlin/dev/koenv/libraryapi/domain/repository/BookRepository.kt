package dev.koenv.libraryapi.domain.repository

import dev.koenv.libraryapi.domain.entity.Book
import java.util.*

interface BookRepository : Repository<Book, UUID> {
    suspend fun findByIsbn(isbn: String): Book?
}
