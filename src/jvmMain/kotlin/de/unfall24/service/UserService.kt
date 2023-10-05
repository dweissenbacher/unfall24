package de.unfall24.service

import com.google.inject.Inject
import de.unfall24.extension.withProfile
import io.ktor.server.application.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class UserService : IUserService {

    @Inject
    lateinit var call: ApplicationCall

    override suspend fun getUser() = call.withProfile { it }
}