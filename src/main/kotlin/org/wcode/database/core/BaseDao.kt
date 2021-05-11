package org.wcode.database.core

import org.jetbrains.exposed.dao.UUIDEntity
import org.wcode.interfaces.BaseDTO
import java.io.Closeable

interface BaseDao<T : UUIDEntity, K : BaseDTO> : Closeable {

    fun insert(instance: K): Result<K>
    fun getAll(): Result<List<K>>
    fun getById(id: String): Result<K>
    fun delete(id: String): Result<Boolean>
}
