package wcode.software.database.base

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.sql.transactions.transaction
import wcode.software.base.BaseDTO

interface BaseDAO<T : BaseDTO, Q : UUIDEntity> {

    fun getAll(): ArrayList<T>

    fun insert(obj: T)

    fun remove(id: String)

    fun update(obj: T)
}