package wcode.software.data.database.daos

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import wcode.software.data.database.schemas.UserSchema
import wcode.software.data.database.tables.UserDB
import wcode.software.data.dtos.UserDTO
import java.lang.Exception

class UserDAO {

    fun insert(userDTO: UserDTO) {
        transaction {
            UserDB.insert {
                it[email] = userDTO.email
                it[password] = userDTO.password
                it[role] = userDTO.role.ordinal
            }
        }
    }

    fun getUser(email: String): UserDTO? {
        return try {
            transaction {
                val user = UserSchema.find { UserDB.email eq email }.first()
                UserDTO(user)
            }
        } catch (e: Exception) {
            null
        }
    }
}