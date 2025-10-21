package dev.koenv.libraryapi.mappers.book

import dev.koenv.libraryapi.domain.entity.Book
import dev.koenv.libraryapi.dto.book.*

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
