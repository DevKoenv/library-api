package dev.koenv.libraryapi.storage.db.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.*

object BooksTable : Table("books") {
    val id = uuid("id").autoGenerate()
    val title = varchar("title", 255)
    val author = varchar("author", 255)
    val isbn = varchar("isbn", 20).uniqueIndex()
    val copiesAvailable = integer("copies_available").default(1)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}
