package de.unfall24.service

import kotlinx.serialization.Serializable

@Serializable
enum class Sort {
    FIRST_NAME, LAST_NAME, EMAIL, FAVOURITE
}