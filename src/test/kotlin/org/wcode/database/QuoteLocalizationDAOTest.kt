package org.wcode.database

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.wcode.database.connections.H2Connection
import org.wcode.database.dao.AuthorDAO
import org.wcode.database.dao.QuoteDAO
import org.wcode.database.dao.QuoteLocalizationDAO
import org.wcode.dto.AuthorDTO

class QuoteLocalizationDAOTest {

    private val database = H2Connection().init()

    private val authorDao = AuthorDAO(database)
    private val quoteLocalizationDAO = QuoteLocalizationDAO(database)
    private val quoteDao = QuoteDAO(database)

    private val author = AuthorDTO(name = "Test", picUrl = "TestPic")

    @Before
    fun initTest() {
        authorDao.insert(author)
    }

    @After
    fun cleanTest() {
        authorDao.delete(author.id)
    }
}
