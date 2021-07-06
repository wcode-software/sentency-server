package org.wcode.database

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.wcode.core.H2Connector
import org.wcode.database.sql.dao.AuthorDAO
import org.wcode.database.sql.dao.QuoteDAO
import org.wcode.dto.AuthorDTO
import org.wcode.dto.QuoteDTO
import org.wcode.dto.QuoteLocalizationDTO
import kotlin.test.assertEquals

class QuoteDAOTest {

    private val database = H2Connector().init()
    private val authorDao = AuthorDAO(database)
    private val quoteDao = QuoteDAO(database)

    private val author = AuthorDTO(id = "Test", name = "Test", picUrl = "TestPic")
    private val quote = QuoteDTO(
        id = "Test", authorId = author.id, messages = listOf(
            QuoteLocalizationDTO(quoteId = "Test", message = "message 1", code = "Te"),
            QuoteLocalizationDTO(quoteId = "Test", message = "message 2", code = "Te2")
        )
    )


    @Before
    fun initTest() {
        authorDao.insert(author)
    }

    @After
    fun cleanTest() {
        authorDao.delete(author.id)
    }

    @Test
    fun `Getting random quote without localization`() {
        quoteDao.insert(quote).onSuccess {
            quoteDao.getRandom().onSuccess {
                assertEquals(2, it.messages.size)
            }.onFailure {
                assert(false)
            }
            quoteDao.delete(quote.id)
        }
    }

    @Test
    fun `Getting random quote with localization`() {
        quoteDao.insert(quote).onSuccess {
            quoteDao.getRandom("Te").onSuccess {
                assertEquals(1, it.messages.size)
                assertEquals(quote.messages[0].message, it.messages[0].message)
            }.onFailure {
                assert(false)
            }
            quoteDao.delete(quote.id)
        }
    }

    @Test
    fun `Getting random quote with not existing localization`() {
        quoteDao.insert(quote).onSuccess {
            quoteDao.getRandom("Te3").onSuccess {
                assertEquals(2, it.messages.size)
            }.onFailure {
                assert(false)
            }
            quoteDao.delete(quote.id)
        }
    }

}
