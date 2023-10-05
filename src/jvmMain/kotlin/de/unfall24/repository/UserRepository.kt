package de.unfall24.repository

import de.unfall24.dao.SqlUserDao
import de.unfall24.dao.UserDao
import de.unfall24.model.User

object UserRepository{

    private val userDao: UserDao = SqlUserDao()

    suspend fun list(): List<User> {
        TODO("Not yet implemented")
    }

    suspend fun remove(user: User) {
        TODO("Not yet implemented")
    }

    suspend fun update(user: User) {
        TODO("Not yet implemented")
    }

    suspend fun add(user: User) {
        userDao.create(user)
    }
}