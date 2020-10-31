package wcode.software.data.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import wcode.software.database.DatabaseConstants

object UserDB : IntIdTable() {
    val email = varchar(DatabaseConstants.User.email, 200).uniqueIndex()
    val password = varchar(DatabaseConstants.User.password, 500)
    val role = integer(DatabaseConstants.User.role)
    val birthday = long(DatabaseConstants.User.birthday).nullable()
    val picUrl = text(DatabaseConstants.User.picUrl).nullable()
}