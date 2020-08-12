package wcode.software.database.controllers

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import wcode.software.database.base.BaseDAO
import wcode.software.database.models.Author
import wcode.software.database.schema.QuoteDB
import wcode.software.dtos.QuoteDTO
import wcode.software.models.Quote
import java.util.*
import kotlin.collections.ArrayList

class QuoteDAO : BaseDAO<QuoteDTO, Quote> {

    override fun getAll(): ArrayList<QuoteDTO> {
        val quotes = arrayListOf<QuoteDTO>()
        transaction {
            Quote.all().forEach { quote ->
                try {
                    quotes.add(QuoteDTO(quote))
                } catch (e: Exception) {
                    quote.delete()
                }
            }
        }
        return quotes
    }

    fun getRandom(): QuoteDTO {
        return QuoteDTO(Quote.all().toList().random())
    }

    override fun insert(obj: QuoteDTO) {
        transaction {
            val mAuthor = Author.findById(UUID.fromString(obj.authorId))
            mAuthor?.let {
                QuoteDB.insert {
                    it[author] = mAuthor.id
                    it[quote] = obj.quote
                }
            }
        }
    }

    override fun remove(id: String) {
        transaction {
            val quote = Quote.findById(UUID.fromString(id))
            quote?.delete()
        }
    }

    override fun update(obj: QuoteDTO) {
        transaction {
            obj.id?.let {
                val quote = Quote.findById(UUID.fromString(it)) ?: return@transaction
                val mAuthor = Author.findById(UUID.fromString(obj.authorId)) ?: return@transaction
                quote.quote = obj.quote
                quote.author = mAuthor
            }
        }
    }
}