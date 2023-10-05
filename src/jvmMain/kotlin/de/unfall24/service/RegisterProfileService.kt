package de.unfall24.service

import de.unfall24.model.User
import de.unfall24.repository.UserRepository

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class RegisterProfileService : IRegisterProfileService {

    override suspend fun registerProfile(user: User): Boolean {
        try {
            UserRepository.add(user)
        } catch (e: Exception) {
            throw Exception("Register operation failed!")
        }
        return true
    }

}