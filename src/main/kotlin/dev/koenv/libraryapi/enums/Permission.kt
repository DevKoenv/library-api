package dev.koenv.libraryapi.enums

enum class Permission {
    // Books
    BOOK_CREATE,
    BOOK_READ,
    BOOK_READ_ALL,
    BOOK_UPDATE,
    BOOK_DELETE,

    // Loans
    LOAN_CREATE,
    LOAN_READ_SELF,
    LOAN_READ_ALL,
    LOAN_UPDATE_SELF,
    LOAN_UPDATE_ALL,
    LOAN_DELETE_SELF,
    LOAN_DELETE_ALL,

    // Users
    USER_CREATE,
    USER_READ_SELF,
    USER_READ_ALL,
    USER_UPDATE_SELF,
    USER_UPDATE_OTHERS,
    USER_DELETE_SELF,
    USER_DELETE_OTHERS,
    USER_ROLE_UPDATE,

    // System / Admin
    SYSTEM_STATS_READ,
    SYSTEM_CONFIG_UPDATE
}
