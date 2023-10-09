package de.unfall24.dao

import de.unfall24.database.Db
import de.unfall24.database.UserTable
import de.unfall24.model.User
import io.ktor.server.auth.*
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class SqlUserDao: UserDao {

    override suspend fun create(user: User): Boolean {
        Db.dbQuery {
            UserTable.insert {
                it[this.name] = user.name!!
                it[this.username] = user.username!!
                it[this.password] = DigestUtils.sha256Hex(user.password!!)
            }
        }

        return true
    }

    override suspend fun read(userId: Int): User? {
        return Db.dbQuery {
            UserTable.select { UserTable.id eq userId }.firstOrNull()?.let {
                User(it[UserTable.id], it[UserTable.name], it[UserTable.username].toString(), null, null)
            }
        }
    }

    override suspend fun update(user: User): User {
        TODO("Not yet implemented")
    }

    override suspend fun delete(userId: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getAuthenticatedUser(principal: UserIdPrincipal): User? {
        return Db.dbQuery {
            UserTable.select { UserTable.username eq principal.name }.firstOrNull()?.let {
                User(it[UserTable.id], it[UserTable.name], it[UserTable.username].toString(), null, null)
            }
        }
    }

    override suspend fun validate(credentials: UserPasswordCredential): Principal? {
        return Db.dbQuery {
            UserTable.select {
                (UserTable.username eq credentials.name) and (UserTable.password eq DigestUtils.sha256Hex(
                    credentials.password
                ))
            }.firstOrNull()?.let {
                UserIdPrincipal(credentials.name)
            }
        }
    }
}