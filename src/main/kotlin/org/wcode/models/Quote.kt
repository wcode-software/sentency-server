package org.wcode.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

@Serializable
data class Quote(val id: String = UUID.randomUUID().toString(), val message: String, val authorId: String) {

    fun toJson(): String {
        return Json.encodeToString(this)
    }
}

val quoteStorage = mutableListOf<Quote>()
