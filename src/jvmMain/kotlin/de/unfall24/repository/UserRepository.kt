package de.unfall24.repository

import de.unfall24.dao.SqlUserDao
import de.unfall24.dao.UserDao
import de.unfall24.model.User
import io.ktor.server.auth.*
import javax.print.attribute.standard.RequestingUserName

object UserRepository{

    private val userDao: UserDao = SqlUserDao()

    suspend fun remove(user: User): Boolean {
        TODO("Not yet implemented")
    }

    suspend fun update(user: User): User {
        TODO("Not yet implemented")
    }

    suspend fun add(user: User): Boolean {
        return userDao.create(user)
    }

    suspend fun get(userId: Int): User? {
        return userDao.read(userId)
    }

    suspend fun getAuthenticatedUser(principal: UserIdPrincipal): User? {
        return userDao.getAuthenticatedUser(principal)
    }

    suspend fun validate(credentials: UserPasswordCredential): Principal? {
        return userDao.validate(credentials)
    }
}