package wcode.software.database.controllers

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import wcode.software.database.base.BaseDAO
import wcode.software.database.models.Author
import wcode.software.database.tables.AuthorDB
import wcode.software.dtos.AuthorDTO
import wcode.software.dtos.QuoteDTO
import wcode.software.models.Quote
import java.util.*
import kotlin.collections.ArrayList

class AuthorDAO : BaseDAO<AuthorDTO, Author> {

    override fun getAll(): ArrayList<AuthorDTO> {
        val authors = arrayListOf<AuthorDTO>()
        transaction {
            authors.addAll(Author.all().map { author ->
                AuthorDTO(author)
            })
        }

        return authors
    }

    override fun insert(obj: AuthorDTO) {
        transaction {
            AuthorDB.insert {
                it[name] = obj.name
                it[picUrl] = obj.picUrl
            }
        }
    }

    override fun remove(id: String) {
        transaction {
            val author = Author.findById(UUID.fromString(id))
            author?.delete()
        }
    }

    override fun update(obj: AuthorDTO) {
        transaction {
            obj.id?.let {
                val author = Author.findById(UUID.fromString(it)) ?: return@transaction
                author.name = obj.name
                author.picUrl = obj.picUrl
            }
        }
    }

    fun getQuotesFromAuthor(authorId: String): ArrayList<QuoteDTO> {
        val quotes = arrayListOf<QuoteDTO>()
        transaction {
            val author = Author.findById(UUID.fromString(authorId)) ?: return@transaction
            author.quotes.forEach { quote ->
                try {
                    quotes.add(QuoteDTO(quote))
                } catch (e: Exception) {
                    quote.delete()
                }
            }
        }
        return quotes
    }
}