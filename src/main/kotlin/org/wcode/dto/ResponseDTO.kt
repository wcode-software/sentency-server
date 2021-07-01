package org.wcode.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.wcode.interfaces.BaseDTO

@Serializable
data class ResponseDTO(
    val success: Boolean = true,
    val error: String? = null
) : BaseDTO {
    fun toJson(): String {
        return Json.encodeToString(this)
    }
}
