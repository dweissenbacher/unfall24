package de.unfall24.extension

import de.unfall24.model.User
import io.ktor.server.application.*
import io.ktor.server.sessions.*

suspend fun <RESP> ApplicationCall.withProfile(block: suspend (User) -> RESP): RESP {
    val user = this.sessions.get<User>()
    return user?.let {
        block(user)
    } ?: throw SecurityException("User not logged in!")
}