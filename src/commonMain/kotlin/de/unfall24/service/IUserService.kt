package de.unfall24.service

import de.unfall24.model.User
import io.kvision.annotations.KVService

@KVService
interface IUserService {
    suspend fun getUser(): User
}