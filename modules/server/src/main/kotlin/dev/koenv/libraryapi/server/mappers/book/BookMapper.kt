package dev.koenv.libraryapi.server.mappers.book

import dev.koenv.libraryapi.server.domain.entity.Book
import dev.koenv.libraryapi.server.dto.book.*

fun Book.toDto(): BookDto = BookDto(
    id = id!!,
    title = title,
    author = author,
    isbn = isbn,
    copiesAvailable = copiesAvailable
)

fun CreateBookDto.toEntity(): Book = Book(
    title = title,
    author = author,
    isbn = isbn,
    copiesAvailable = copiesAvailable
)

fun UpdateBookDto.applyTo(existing: Book): Book = existing.copy(
    title = title ?: existing.title,
    author = author ?: existing.author,
    isbn = isbn ?: existing.isbn,
    copiesAvailable = copiesAvailable ?: existing.copiesAvailable
)
