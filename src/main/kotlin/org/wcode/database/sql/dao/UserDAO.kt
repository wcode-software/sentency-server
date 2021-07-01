package org.wcode.database.sql.dao

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.wcode.core.Cryptography.decrypt
import org.wcode.core.Cryptography.encrypt
import org.wcode.core.Security.makeToken
import org.wcode.database.sql.core.BaseDao
import org.wcode.database.sql.schema.UserSchema
import org.wcode.database.sql.tables.UserTable
import org.wcode.dto.AuthDTO
import org.wcode.dto.AuthorDTO
import org.wcode.dto.UserDTO
import java.util.*

class UserDAO(private val db: Database) : BaseDao<UserDTO> {

    override fun insert(instance: UserDTO): Result<UserDTO> = transaction(db) {
        try {
            val mUserDTO = UserSchema.new(UUID.fromString(instance.id)) {
                this.email = instance.email
                this.password = instance.password.encrypt()
            }
            Result.success(mUserDTO.toDTO())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun list(page: Int, size: Int, all: Boolean): Result<List<UserDTO>> = transaction(db) {
        try {
            var mUsers = UserSchema.all()
            if (!all) {
                mUsers = mUsers.limit(size, offset = (page - 1).toLong())
            }
            Result.success(mUsers.toList().map { it.toDTO() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getById(id: String): Result<UserDTO> = transaction(db) {
        try {
            val mUser = UserSchema.findById(UUID.fromString(id))
            if (mUser != null) {
                Result.success(mUser.toDTO())
            } else {
                Result.failure(NoSuchElementException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun delete(id: String): Result<Boolean> = transaction(db) {
        try {
            val mUser = UserSchema.findById(UUID.fromString(id))
            if (mUser != null) {
                mUser.delete()
                Result.success(true)
            } else {
                Result.failure(NoSuchElementException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun update(instance: UserDTO): Result<UserDTO> = transaction(db) {
        try {
            UserSchema.findById(UUID.fromString(instance.id))?.let { current ->
                current.password = instance.password.encrypt()
                Result.success(current.toDTO())
            } ?: Result.failure(NoSuchElementException())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun count(): Int = transaction(db) {
        UserSchema.all().count().toInt()
    }

    fun login(username: String, password: String): Result<AuthDTO> = transaction(db) {
        try {
            UserSchema.find { UserTable.email eq username }.first().let { user ->
                if (password == user.password.decrypt()) {
                    val auth = AuthDTO(user.toDTO().makeToken())
                    Result.success(auth)
                } else {
                    Result.failure(Exception("Auth error"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun userExist(id: String): Boolean = transaction(db) {
        val mUser = UserSchema.findById(UUID.fromString(id))
        mUser != null
    }
}
