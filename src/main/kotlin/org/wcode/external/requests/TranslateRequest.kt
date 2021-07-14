package org.wcode.external.requests

import kotlinx.serialization.Serializable

@Serializable
data class TranslateRequest(
    val q: String,
    val source: String = "en",
    val target: String,
    val format: String = "text"
) {
    constructor(message: String, code: String) : this(
        target = code.split("-")[0],
        q = message
    )
}
