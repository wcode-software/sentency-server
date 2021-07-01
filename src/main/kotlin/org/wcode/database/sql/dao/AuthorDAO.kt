package org.wcode.database.sql.dao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.wcode.database.sql.core.BaseDao
import org.wcode.database.sql.schema.AuthorSchema
import org.wcode.database.sql.tables.AuthorTable
import org.wcode.database.sql.tables.QuoteTable
import org.wcode.dto.AuthorDTO
import org.wcode.dto.QuoteDTO
import java.util.*

class AuthorDAO(private val db: Database) : BaseDao<AuthorDTO> {

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

    override fun list(page: Int, size: Int, all: Boolean): Result<List<AuthorDTO>> = transaction(db) {
        try {
            var mAuthors = AuthorSchema.all()
            if (!all) {
                mAuthors = mAuthors.limit(size, offset = (page - 1).toLong())
            }
            Result.success(mAuthors.toList().map { it.toDTO() })
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
                Result.failure(NoSuchElementException())
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
                Result.failure(NoSuchElementException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun update(instance: AuthorDTO): Result<AuthorDTO> = transaction(db) {
        try {
            AuthorSchema.findById(UUID.fromString(instance.id))?.let { current ->
                current.name = instance.name
                current.picUrl = instance.picUrl
                Result.success(current.toDTO())
            } ?: Result.failure(NoSuchElementException())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun count(): Int = transaction(db) {
        AuthorSchema.all().count().toInt()
    }

    fun getAuthorWithMostQuotes(): Result<AuthorDTO> = transaction(db) {
        try {
            val expression = wrapAsExpression<Int>(QuoteTable.slice(QuoteTable.id.count()).select {
                AuthorTable.id eq QuoteTable.author
            })

            val collection = AuthorSchema.all().orderBy(Pair(expression, SortOrder.DESC))
            when {
                collection.count() >= 2 -> Result.success(collection.drop(1).first().toDTO())
                collection.count() > 0 -> Result.success(collection.first().toDTO())
                else -> Result.failure(NoSuchElementException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllAuthorQuotes(id: String): Result<List<QuoteDTO>> = transaction(db) {
        try {
            AuthorSchema.findById(UUID.fromString(id))?.let { current ->
                val quotes = current.quotes.map { it.toDTO() }
                Result.success(quotes)
            } ?: Result.failure(NoSuchElementException())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
