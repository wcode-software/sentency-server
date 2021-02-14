package wcode.software.base

import org.jetbrains.exposed.dao.UUIDEntity

interface BaseDAO<T : BaseDTO, Q : UUIDEntity> {

    fun getAll(): ArrayList<T>

    fun insert(obj: T)

    fun remove(id: String)

    fun update(obj: T)

    fun getCount(): Int
}
