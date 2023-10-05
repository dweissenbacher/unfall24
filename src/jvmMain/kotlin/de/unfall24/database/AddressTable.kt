package de.unfall24.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object AddressTable : Table("address") {
    val id = integer("id").primaryKey().autoIncrement()
    val firstName = varchar("first_name", 255).nullable()
    val lastName = varchar("last_name", 255).nullable()
    val email = varchar("email", 255).nullable()
    val phone = varchar("phone", 255).nullable()
    val postalAddress = varchar("postal_address", 255).nullable()
    val favourite = bool("favourite")
    val createdAt = datetime("created_at").nullable()
    val userId = reference("user_id", UserTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
}