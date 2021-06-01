package org.wcode.database.dao

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.wcode.database.core.BaseDao
import org.wcode.database.schema.AuthorSchema
import org.wcode.database.schema.QuoteSchema
import org.wcode.dto.AuthorDTO
import org.wcode.dto.QuoteDTO
import java.util.*
import kotlin.math.max

class QuoteDAO(private val db: Database) : BaseDao<QuoteDTO> {

    override fun insert(instance: QuoteDTO): Result<QuoteDTO> = transaction(db) {
        try {
            val mAuthor = AuthorSchema.findById(UUID.fromString(instance.authorId))
            mAuthor?.let {
                val mQuote = QuoteSchema.new(UUID.fromString(instance.id)) {
                    this.author = mAuthor
                    this.message = instance.message
                    this.timestamp = Calendar.getInstance().timeInMillis
                }
                Result.success(mQuote.toDTO())
            } ?: Result.failure(NullPointerException())

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun count(): Int = transaction(db) {
        QuoteSchema.all().count().toInt()
    }

    override fun list(page: Int, size: Int, all: Boolean): Result<List<QuoteDTO>> = transaction(db) {
        try {
            var mQuotes = QuoteSchema.all()
            if (!all) {
                mQuotes = mQuotes.limit(size, (page - 1).toLong())
            }
            Result.success(mQuotes.toList().map { it.toDTO() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getById(id: String): Result<QuoteDTO> = transaction(db) {
        try {
            val mQuote = QuoteSchema.findById(UUID.fromString(id))
            if (mQuote != null) {
                Result.success(mQuote.toDTO())
            } else {
                Result.failure(NullPointerException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun delete(id: String): Result<Boolean> = transaction(db) {
        try {
            val mQuote = QuoteSchema.findById(UUID.fromString(id))
            if (mQuote != null) {
                mQuote.delete()
                Result.success(true)
            } else {
                Result.failure(NullPointerException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun update(instance: QuoteDTO): Result<QuoteDTO> = transaction(db) {
        try {
            val mAuthor = AuthorSchema.findById(UUID.fromString(instance.authorId))
            val mQuote = QuoteSchema.findById(UUID.fromString(instance.id))
            if (mAuthor != null && mQuote != null) {
                mQuote.author = mAuthor
                mQuote.message = instance.message
                mQuote.timestamp = instance.timestamp
                Result.success(mQuote.toDTO())
            } else {
                Result.failure(NullPointerException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
