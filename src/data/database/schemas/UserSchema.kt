package wcode.software.data.database.schemas

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import wcode.software.data.database.tables.UserDB

class UserSchema(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<UserSchema>(UserDB)

    var email by UserDB.email
    var password by UserDB.password
    var role by UserDB.role
}