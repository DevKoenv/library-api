package dev.koenv.libraryapi.enums

enum class Permission {
    // Books
    BOOK_CREATE,
    BOOK_READ,
    BOOK_UPDATE,
    BOOK_DELETE,

    // Loans
    LOAN_CREATE,
    LOAN_READ,
    LOAN_UPDATE,
    LOAN_DELETE,

    // Users
    USER_READ,
    USER_UPDATE,
    USER_ROLE_UPDATE
}
