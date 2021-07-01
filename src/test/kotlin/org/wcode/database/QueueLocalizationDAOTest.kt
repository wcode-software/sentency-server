package org.wcode.database

import org.junit.Test
import org.wcode.core.MockMongoConnector
import org.wcode.database.nosql.dao.QueueLocalizationDAO
import org.wcode.dto.QuoteLocalizationDTO
import kotlin.test.assertEquals

class QueueLocalizationDAOTest {

    private val database = MockMongoConnector().init()
    private val queueLocalizationDAO = QueueLocalizationDAO(database)
    private val quoteLocalizationDTO =
        QuoteLocalizationDTO(code = "test", message = "message of test", quoteId = "id of the quote")

    @Test
    fun `Insert quote into queue`() {
        queueLocalizationDAO.insert(quoteLocalizationDTO)
        assertEquals(1, queueLocalizationDAO.listAll().size)
        queueLocalizationDAO.drop(quoteLocalizationDTO.id)
    }

    @Test
    fun `Delete quote from queue`() {
        queueLocalizationDAO.insert(quoteLocalizationDTO)
        queueLocalizationDAO.drop(quoteLocalizationDTO.id)
        assertEquals(0, queueLocalizationDAO.listAll().size)
    }

    @Test
    fun `List all localizations`() {
        val localizations = listOf(
            QuoteLocalizationDTO(code = "test", message = "message of test", quoteId = "id of the quote"),
            QuoteLocalizationDTO(code = "test", message = "message of test", quoteId = "id of the quote"),
            QuoteLocalizationDTO(code = "test", message = "message of test", quoteId = "id of the quote")
        )
        localizations.forEach { locale ->
            queueLocalizationDAO.insert(locale)
        }
        assertEquals(3, queueLocalizationDAO.listAll().size)
        localizations.forEach { locale ->
            queueLocalizationDAO.drop(locale.id)
        }
    }

}
