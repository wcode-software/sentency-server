package org.wcode.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.wcode.interfaces.BaseDTO
import java.util.*

@Serializable
data class AuthorDTO(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val picUrl: String? = null
) : BaseDTO {

    fun toJson(): String {
        return Json.encodeToString(this)
    }
}
