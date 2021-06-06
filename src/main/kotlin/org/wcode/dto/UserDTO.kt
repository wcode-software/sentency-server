package org.wcode.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.wcode.interfaces.BaseDTO
import java.util.*

@Serializable
data class UserDTO(
    val id: String = UUID.randomUUID().toString(),
    val email: String,
    val password: String
) : BaseDTO {

    fun toJson(): String {
        return Json.encodeToString(this)
    }
}
