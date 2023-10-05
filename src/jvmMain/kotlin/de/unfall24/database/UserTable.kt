package de.unfall24.database

import org.jetbrains.exposed.sql.Table

object UserTable : Table("users") {
    val id = integer("id").primaryKey().autoIncrement()
    val name = varchar("name", 255)
    val username = varchar("username", 255).uniqueIndex()
    val password = varchar("password", 255)
}