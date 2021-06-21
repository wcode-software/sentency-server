package org.wcode.database.dao

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.wcode.database.core.BaseDao
import org.wcode.database.schema.AuthorSchema
import org.wcode.database.schema.QuoteLocalizationSchema
import org.wcode.database.schema.QuoteSchema
import org.wcode.database.tables.QuoteLocalizationTable
import org.wcode.database.tables.QuoteTable
import org.wcode.dto.AuthorDTO
import org.wcode.dto.QuoteDTO
import org.wcode.utils.CalendarUtils
import java.util.*
import kotlin.math.max

class QuoteDAO(private val db: Database) : BaseDao<QuoteDTO> {

    override fun insert(instance: QuoteDTO): Result<QuoteDTO> = transaction(db) {
        try {
            val mAuthor = AuthorSchema.findById(UUID.fromString(instance.authorId))
            mAuthor?.let {
                val mQuote = QuoteSchema.new(UUID.fromString(instance.id)) {
                    this.author = mAuthor
                    this.timestamp = instance.timestamp
                    this.type = instance.type
                }
                instance.messages.forEach { message ->
                    QuoteLocalizationSchema.new(UUID.fromString(message.id)) {
                        this.code = message.code
                        this.message = message.message
                        this.quote = mQuote
                    }
                }
                Result.success(mQuote.toDTO())
            } ?: Result.failure(NoSuchElementException())
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
                Result.failure(NoSuchElementException())
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
                Result.failure(NoSuchElementException())
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
                instance.messages.forEach { message ->
                    val mMessage = QuoteLocalizationSchema.findById(UUID.fromString(message.id))
                        ?: QuoteLocalizationSchema.new(
                            UUID.fromString(message.id)
                        ) {}
                    mMessage.code = message.code
                    mMessage.message = message.message
                }
                mQuote.timestamp = instance.timestamp
                mQuote.type = instance.type
                Result.success(mQuote.toDTO())
            } else {
                Result.failure(NoSuchElementException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getRandom(languageCode: String = "None"): Result<QuoteDTO> = transaction(db) {
        try {
            val quote = QuoteSchema.all().toList().random().toDTO()
            val messages = quote.messages.filter { it.code == languageCode }
            if (messages.isNotEmpty()) {
                quote.messages = messages
            }
            Result.success(quote)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getMonthCount(): Result<Int> = transaction(db) {
        try {
            val (start, end) = CalendarUtils.getMonthStartEnd()
            val count = QuoteTable.select {
                (QuoteTable.timestap greaterEq start) and (QuoteTable.timestap lessEq end)
            }.count().toInt()
            Result.success(count)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
