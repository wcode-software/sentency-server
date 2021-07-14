package org.wcode.database

import org.junit.Test
import org.wcode.core.MockMongoConnector
import org.wcode.database.nosql.dao.TranslationDAO
import org.wcode.database.nosql.models.TranslationMongo
import kotlin.test.assertEquals

class TranslationDAOTest {

    private val database = MockMongoConnector().init()
    private val translationDAO = TranslationDAO(database)
    private val translationMongo = TranslationMongo(quoteId = "teste", code = "test", message = "test translation")

    @Test
    fun `Test insert Translation`() {
        translationDAO.insert(translationMongo)
        val queryResult = translationDAO.getByCodeId(translationMongo.code, translationMongo.quoteId)
        assertEquals(true, queryResult.isSuccess)
        translationDAO.drop(translationMongo.code, translationMongo.quoteId)
    }

    @Test
    fun `Test drop Translation`() {
        translationDAO.insert(translationMongo)
        val queryResult = translationDAO.getByCodeId(translationMongo.code, translationMongo.quoteId)
        assertEquals(true, queryResult.isSuccess)
        assertEquals(true, translationDAO.drop(translationMongo.code, translationMongo.quoteId))
        val queryResult2 = translationDAO.getByCodeId(translationMongo.code, translationMongo.quoteId)
        assertEquals(false, queryResult2.isSuccess)
    }
}
