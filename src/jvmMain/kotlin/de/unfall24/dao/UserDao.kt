package de.unfall24.dao

import de.unfall24.model.User
import io.ktor.server.auth.*

interface UserDao {

    suspend fun create(user: User): Boolean

    suspend fun read(userId: Int): User?

    suspend fun update(user: User): User

    suspend fun delete(userId: Int): Boolean

    suspend fun getAuthenticatedUser(principal: UserIdPrincipal): User?

    suspend fun validate(credentials: UserPasswordCredential): Principal?

}