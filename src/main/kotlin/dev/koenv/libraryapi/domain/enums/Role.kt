package dev.koenv.libraryapi.domain.enums

import dev.koenv.libraryapi.enums.Permission
import kotlinx.serialization.Serializable

@Serializable
enum class Role(val permissions: Set<Permission>) {
    ADMIN(
        Permission.entries.toSet()
    ),

    LIBRARIAN(
        setOf(
            // Books
            Permission.BOOK_CREATE,
            Permission.BOOK_READ,
            Permission.BOOK_UPDATE,
            Permission.BOOK_DELETE,

            // Loans
            Permission.LOAN_CREATE,
            Permission.LOAN_READ_SELF,
            Permission.LOAN_READ_OTHERS,
            Permission.LOAN_UPDATE_SELF,
            Permission.LOAN_UPDATE_OTHERS,
            Permission.LOAN_DELETE_SELF,
            Permission.LOAN_DELETE_OTHERS,

            // Users
            Permission.USER_READ_SELF,
            Permission.USER_READ_OTHERS,
            Permission.USER_UPDATE_SELF,
            Permission.USER_UPDATE_OTHERS,
            Permission.USER_DELETE_SELF,
            Permission.USER_DELETE_OTHERS,
            Permission.USER_ROLE_UPDATE
        )
    ),

    MEMBER(
        setOf(
            // Books
            Permission.BOOK_READ,

            // Loans
            Permission.LOAN_CREATE,
            Permission.LOAN_READ_SELF,
            Permission.LOAN_UPDATE_SELF,
            Permission.LOAN_DELETE_SELF,

            // Users
            Permission.USER_READ_SELF,
            Permission.USER_UPDATE_SELF,
            Permission.USER_DELETE_SELF
        )
    );
}