package de.unfall24.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val name: String? = null,
    val username: String? = null,
    val password: String? = null,
    val password2: String? = null
)