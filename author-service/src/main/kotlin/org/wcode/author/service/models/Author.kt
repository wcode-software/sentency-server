package org.wcode.author.service.models

import io.micronaut.core.annotation.Introspected
import java.util.*

@Introspected
data class Author(
    var id: String? = null,
    val name: String,
    val picUrl: String? = null
) {

    init {
        id = id ?: UUID.randomUUID().toString()
    }

}
