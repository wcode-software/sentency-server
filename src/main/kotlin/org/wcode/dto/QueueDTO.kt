package org.wcode.dto

import kotlinx.serialization.Serializable
import org.wcode.interfaces.BaseDTO

@Serializable
data class QueueDTO<T : BaseDTO>(
    val data: T,
    val timestamp: Long,
    val id: String
) : BaseDTO
