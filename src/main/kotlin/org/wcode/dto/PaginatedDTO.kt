package org.wcode.dto

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedDTO<T>(
    val data: T,
    val page: Int,
    val size: Int
) {
    constructor(
        data: T,
        page: Int,
        size: Int,
        path: String,
        total: Int
    ) : this(data, page, size) {
        prev = getPrevUrl(path)
        next = getNextUrl(path, total)
    }

    var prev: String? = null
        private set

    var next: String? = null
        private set

    private fun getPrevUrl(path: String): String? {
        return if (page > 1) {
            "$path?${listOf("page" to "${page - 1}", "size" to "$size").formUrlEncode()}"
        } else null
    }

    private fun getNextUrl(path: String, total: Int): String? {
        return if (page * size < total) {
            "$path?${listOf("page" to "${page + 1}", "size" to "$size").formUrlEncode()}"
        } else null
    }
}
