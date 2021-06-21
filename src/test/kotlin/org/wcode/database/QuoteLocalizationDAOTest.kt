package org.wcode.database

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.wcode.database.connections.H2Connection
import org.wcode.database.dao.AuthorDAO
import org.wcode.database.dao.QuoteDAO
import org.wcode.database.dao.QuoteLocalizationDAO
import org.wcode.dto.AuthorDTO
import org.wcode.dto.QuoteDTO
import org.wcode.dto.QuoteLocalizationDTO
import kotlin.test.assertEquals

class QuoteLocalizationDAOTest {

    private val database = H2Connection().init()

    private val authorDao = AuthorDAO(database)
    private val quoteLocalizationDAO = QuoteLocalizationDAO(database)
    private val quoteDao = QuoteDAO(database)

    private val author = AuthorDTO(name = "Test", picUrl = "TestPic")
    private val quote = QuoteDTO(authorId = author.id)

    @Before
    fun initTest() {
        authorDao.insert(author)
        quoteDao.insert(quote)
    }

    @After
    fun cleanTest() {
        authorDao.delete(author.id)
        quoteDao.delete(quote.id)
    }


    @Test
    fun `List all quotes localization`(){
        val localizations = listOf(
            QuoteLocalizationDTO(code =  "te", message = "Test", quoteId = quote.id),
            QuoteLocalizationDTO(code =  "te2", message = "Test", quoteId = quote.id),
            QuoteLocalizationDTO(code =  "te3", message = "Test", quoteId = quote.id)
        )

        localizations.forEach { locale ->
            quoteLocalizationDAO.insert(locale)
        }

        quoteLocalizationDAO.list(all = true).onSuccess {
            assertEquals(3,it.size)
        }.onFailure {
            assert(false)
        }

        localizations.forEach { locale ->
            quoteLocalizationDAO.delete(locale.id)
        }
    }

    @Test
    fun `List all quotes localization paginated`(){
        val localizations = listOf(
            QuoteLocalizationDTO(code =  "te", message = "Test", quoteId = quote.id),
            QuoteLocalizationDTO(code =  "te2", message = "Test", quoteId = quote.id),
            QuoteLocalizationDTO(code =  "te3", message = "Test", quoteId = quote.id)
        )

        localizations.forEach { locale ->
            quoteLocalizationDAO.insert(locale)
        }

        quoteLocalizationDAO.list(page = 1, size = 2).onSuccess {
            assertEquals(2,it.size)
        }.onFailure {
            assert(false)
        }

        localizations.forEach { locale ->
            quoteLocalizationDAO.delete(locale.id)
        }
    }

    @Test
    fun `Get localization by ID`(){
        val locale = QuoteLocalizationDTO(code =  "te", message = "Test", quoteId = quote.id)

        quoteLocalizationDAO.insert(locale).onSuccess {
            assertEquals(locale.message,it.message)
        }.onFailure {
            assert(false)
        }

        quoteLocalizationDAO.getById(locale.id).onSuccess {
            assertEquals(locale.message,it.message)
        }.onFailure {
            assert(false)
        }

        quoteLocalizationDAO.delete(locale.id)
    }

    @Test
    fun `Count quote localization`(){
        val localizations = listOf(
            QuoteLocalizationDTO(code =  "te", message = "Test", quoteId = quote.id),
            QuoteLocalizationDTO(code =  "te2", message = "Test", quoteId = quote.id),
            QuoteLocalizationDTO(code =  "te3", message = "Test", quoteId = quote.id)
        )

        localizations.forEach { locale ->
            quoteLocalizationDAO.insert(locale)
        }

        assertEquals(3,quoteLocalizationDAO.count())

        localizations.forEach { locale ->
            quoteLocalizationDAO.delete(locale.id)
        }
    }
}
