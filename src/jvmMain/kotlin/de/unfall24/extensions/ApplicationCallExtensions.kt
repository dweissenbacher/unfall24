package de.unfall24.extensions

import de.unfall24.model.Profile
import io.ktor.server.application.*
import io.ktor.server.sessions.*

suspend fun <RESP> ApplicationCall.withProfile(block: suspend (Profile) -> RESP): RESP {
    val profile = this.sessions.get<Profile>()
    return profile?.let {
        block(profile)
    } ?: throw IllegalStateException("Profile not set!")
}