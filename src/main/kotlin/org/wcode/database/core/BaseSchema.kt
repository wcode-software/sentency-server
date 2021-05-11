package org.wcode.database.core

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.wcode.interfaces.BaseDTO
import java.util.*

abstract class BaseSchema<T : BaseDTO>(id: EntityID<UUID>) : UUIDEntity(id) {

    abstract fun toDTO(): T
}
