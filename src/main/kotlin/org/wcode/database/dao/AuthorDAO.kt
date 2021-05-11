package org.wcode.database.dao

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.wcode.database.core.BaseDao
import org.wcode.database.schema.AuthorSchema
import org.wcode.dto.AuthorDTO
import java.util.*

class AuthorDAO(private val db: Database) : BaseDao<AuthorSchema, AuthorDTO> {

    override fun insert(instance: AuthorDTO): Result<AuthorDTO> = transaction(db) {
        try {
            val mAuthorDTO = AuthorSchema.new(UUID.fromString(instance.id)) {
                this.name = instance.name
                this.picUrl = instance.picUrl
            }
            Result.success(mAuthorDTO.toDTO())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAll(): Result<List<AuthorDTO>> = transaction(db) {
        try {
            val mAuthors = AuthorSchema.all().toList().map { it.toDTO() }
            Result.success(mAuthors)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getById(id: String): Result<AuthorDTO> = transaction(db) {
        try {
            val mAuthor = AuthorSchema.findById(UUID.fromString(id))
            if (mAuthor != null) {
                Result.success(mAuthor.toDTO())
            } else {
                Result.failure(NullPointerException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun delete(id: String): Result<Boolean> = transaction(db) {
        try {
            val mAuthor = AuthorSchema.findById(UUID.fromString(id))
            if (mAuthor != null) {
                mAuthor.delete()
                Result.success(true)
            } else {
                Result.failure(NullPointerException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun close() {}
}
