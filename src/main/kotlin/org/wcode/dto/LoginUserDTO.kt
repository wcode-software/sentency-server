package org.wcode.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class LoginUserDTO(
    val email: String,
    val password: String
) {
    fun toJson(): String {
        return Json.encodeToString(this)
    }
}
