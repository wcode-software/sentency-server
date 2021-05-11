package org.wcode.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.wcode.interfaces.BaseDTO
import java.util.*

@Serializable
data class QuoteDTO(
    val id: String = UUID.randomUUID().toString(),
    val message: String,
    val authorId: String,
    val timestamp: Long = Calendar.getInstance().timeInMillis
) : BaseDTO {

    fun toJson(): String {
        return Json.encodeToString(this)
    }
}
