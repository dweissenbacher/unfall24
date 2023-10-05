package de.unfall24.service

import de.unfall24.model.Profile
import io.kvision.annotations.KVService

@KVService
interface IProfileService {
    suspend fun getProfile(): Profile
}