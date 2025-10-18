package dev.koenv.libraryapi.storage.db.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object LoansTable : Table("loans") {
    val id = uuid("id").autoGenerate()
    val userId = reference("user_id", UsersTable.id)
    val bookId = reference("book_id", BooksTable.id)
    val borrowedAt = datetime("borrowed_at").defaultExpression(CurrentDateTime)
    val returnedAt = datetime("returned_at").nullable()

    override val primaryKey = PrimaryKey(id)
}
