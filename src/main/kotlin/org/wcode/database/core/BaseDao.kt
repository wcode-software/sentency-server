package org.wcode.database.core

import org.wcode.interfaces.BaseDTO

interface BaseDao<K : BaseDTO> {
    fun insert(instance: K): Result<K>
    fun getAll(): Result<List<K>>
    fun getById(id: String): Result<K>
    fun delete(id: String): Result<Boolean>
    fun update(instance: K): Result<K>
}
