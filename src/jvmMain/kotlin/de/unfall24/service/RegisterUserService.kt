package de.unfall24.service

import de.unfall24.model.User
import de.unfall24.repository.UserRepository

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class RegisterUserService : IRegisterUserService {

    override suspend fun registerUser(user: User): Boolean {
        try {
            return UserRepository.add(user)
        } catch (e: Exception) {
            throw Exception("Register operation failed!")
        }
    }
}