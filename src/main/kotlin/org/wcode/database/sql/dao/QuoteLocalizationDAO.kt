package org.wcode.database.sql.dao

import io.ktor.features.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.wcode.database.sql.core.BaseDao
import org.wcode.database.sql.schema.QuoteLocalizationSchema
import org.wcode.database.sql.schema.QuoteSchema
import org.wcode.dto.QuoteLocalizationDTO
import java.lang.RuntimeException
import java.util.*

class QuoteLocalizationDAO(private val db: Database) : BaseDao<QuoteLocalizationDTO> {

    override fun insert(instance: QuoteLocalizationDTO): Result<QuoteLocalizationDTO> =
        transaction(db) {
            try {
                val mQuote = QuoteSchema.findById(UUID.fromString(instance.quoteId))
                mQuote?.let {
                    val localization = it.messages.find { message -> message.code == instance.code }
                    if (localization != null) {
                        Result.failure(RuntimeException())
                    } else {
                        val mQuotesLocalization = QuoteLocalizationSchema.new(UUID.fromString(instance.id)) {
                            this.code = instance.code
                            this.message = instance.message
                            this.quote = mQuote
                        }
                        Result.success(mQuotesLocalization.toDTO())
                    }
                } ?: Result.failure(NotFoundException())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override fun list(page: Int, size: Int, all: Boolean): Result<List<QuoteLocalizationDTO>> = transaction(db) {
        try {
            var mQuotesLocalization = QuoteLocalizationSchema.all()
            if (!all) {
                mQuotesLocalization = mQuotesLocalization.limit(size, (page - 1).toLong())
            }
            Result.success(mQuotesLocalization.toList().map { it.toDTO() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getById(id: String): Result<QuoteLocalizationDTO> = transaction(db) {
        try {
            val mQuoteLocalization = QuoteLocalizationSchema.findById(UUID.fromString(id))
            if (mQuoteLocalization != null) {
                Result.success(mQuoteLocalization.toDTO())
            } else {
                Result.failure(NoSuchElementException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun delete(id: String): Result<Boolean> = transaction(db) {
        try {
            val mQuoteLocalization = QuoteLocalizationSchema.findById(UUID.fromString(id))
            if (mQuoteLocalization != null) {
                mQuoteLocalization.delete()
                Result.success(true)
            } else {
                Result.failure(NoSuchElementException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun update(instance: QuoteLocalizationDTO): Result<QuoteLocalizationDTO> = transaction(db) {
        try {
            val mQuote = QuoteSchema.findById(UUID.fromString(instance.quoteId))
            mQuote?.let {
                val mQuoteLocalization = QuoteLocalizationSchema.findById(UUID.fromString(instance.id))
                if (mQuoteLocalization != null) {
                    mQuoteLocalization.code = instance.code
                    mQuoteLocalization.message = instance.message
                    mQuoteLocalization.quote = mQuote
                    Result.success(mQuoteLocalization.toDTO())
                } else {
                    Result.failure(NoSuchElementException())
                }
            } ?: Result.failure(NotFoundException())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun count(): Int = transaction(db) {
        QuoteLocalizationSchema.count().toInt()
    }

    fun countQuoteLocalizations(quoteId: String): Int = transaction(db) {
        QuoteSchema.findById(UUID.fromString(quoteId))?.messages?.count()?.toInt() ?: -1
    }

    fun getAllQuoteLocalizations(quoteId: String): Result<List<QuoteLocalizationDTO>> = transaction(db) {
        try {
            val mQuote = QuoteSchema.findById(UUID.fromString(quoteId))
            mQuote?.let {
                Result.success(mQuote.messages.map { it.toDTO() })
            } ?: Result.failure(NotFoundException())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
