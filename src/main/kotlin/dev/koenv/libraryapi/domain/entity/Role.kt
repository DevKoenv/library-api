package dev.koenv.libraryapi.domain.entity

import dev.koenv.libraryapi.enums.Permission
import kotlinx.serialization.Serializable

@Serializable
enum class Role(val permissions: Set<Permission>) {
    ADMIN(
        Permission.entries.toSet()
    ),

    LIBRARIAN(
        setOf(
            Permission.BOOK_CREATE,
            Permission.BOOK_READ,
            Permission.BOOK_UPDATE,
            Permission.BOOK_DELETE,
            Permission.LOAN_READ,
            Permission.LOAN_UPDATE,
            Permission.USER_READ
        )
    ),

    MEMBER(
        setOf(
            Permission.BOOK_READ,
            Permission.LOAN_CREATE,
            Permission.LOAN_READ,
            Permission.LOAN_UPDATE
        )
    );
}
