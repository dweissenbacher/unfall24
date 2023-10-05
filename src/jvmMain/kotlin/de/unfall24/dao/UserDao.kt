package de.unfall24.dao

import de.unfall24.model.User
import de.unfall24.service.Sort

interface UserDao {

    suspend fun create(user: User)

    suspend fun read(id: Int): User

    suspend fun update(user: User)

    suspend fun delete(user: User)

    suspend fun search(search: String?, types: String, sort: Sort) :List<User>

}