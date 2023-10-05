package de.unfall24.service

import de.unfall24.model.User
import io.kvision.annotations.KVService

@KVService
interface IRegisterProfileService {
    suspend fun registerProfile(user: User): Boolean
}