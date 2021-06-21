package org.wcode.routes

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.wcode.core.setupTestApplication
import org.wcode.dto.AuthorDTO
import org.wcode.dto.PaginatedDTO
import org.wcode.dto.QuoteDTO
import org.wcode.dto.QuoteLocalizationDTO
import java.util.*
import kotlin.test.assertEquals

class QuoteRoutesTest {

    private val quoteId = UUID.randomUUID().toString()
    private val messages = listOf(QuoteLocalizationDTO(message = "Test", code = "test", quoteId = quoteId))
    private val author = AuthorDTO(id = UUID.randomUUID().toString(), name = "Test")

    @Before
    fun setupTest() {
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/author") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(author.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @After
    fun cleanTest() {
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Delete, "/author/${author.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("Author removed correctly", response.content)
                }
            }
        }
    }

    @Test
    fun `Add Quote with ID`() {
        withTestApplication({ setupTestApplication() }) {
            val quote = QuoteDTO(id = quoteId, messages = messages, authorId = author.id)
            handleRequest(HttpMethod.Post, "/quote") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Delete, "/quote/${quote.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("Quote removed correctly", response.content)
                }
            }
        }
    }

    @Test
    fun `Add Quote without ID`() {
        withTestApplication({ setupTestApplication() }) {
            val quote = QuoteDTO(messages = messages, authorId = author.id)
            var quoteId = ""
            handleRequest(HttpMethod.Post, "/quote") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    val responseQuote = Json.decodeFromString<QuoteDTO>(it)
                    quoteId = responseQuote.id
                }
            }

            handleRequest(HttpMethod.Delete, "/quote/$quoteId") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("Quote removed correctly", response.content)
                }
            }
        }
    }

    @Test
    fun `Get all Quotes empty list`() {
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Get, "/quote/") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    val responseQuote = Json.decodeFromString<List<QuoteDTO>>(it)
                    assertEquals(responseQuote.size, 0)
                }
            }
        }
    }

    @Test
    fun `Get all Quotes list with one quote`() {
        withTestApplication({ setupTestApplication() }) {
            val quote = QuoteDTO(id = quoteId, messages = messages, authorId = author.id)
            handleRequest(HttpMethod.Post, "/quote") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/quote/all") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    val responseQuote = Json.decodeFromString<List<QuoteDTO>>(it)
                    assertEquals(1, responseQuote.size)
                }
            }

            handleRequest(HttpMethod.Delete, "/quote/${quote.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("Quote removed correctly", response.content)
                }
            }
        }
    }

    @Test
    fun `Get Quote by ID`() {
        withTestApplication({ setupTestApplication() }) {
            val quote = QuoteDTO(id = quoteId, messages = messages, authorId = author.id)
            handleRequest(HttpMethod.Post, "/quote") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/quote/${quote.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    val responseQuote = Json.decodeFromString<QuoteDTO>(it)
                    assertEquals(responseQuote.id, quote.id)
                    assertEquals(responseQuote.messages[0].message, quote.messages[0].message)
                    assertEquals(responseQuote.authorId, quote.authorId)
                }
            }

            handleRequest(HttpMethod.Delete, "/quote/${quote.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("Quote removed correctly", response.content)
                }
            }
        }
    }

    @Test
    fun `Delete Quote by ID`() {
        withTestApplication({ setupTestApplication() }) {
            val quote = QuoteDTO(id = quoteId, messages = messages, authorId = author.id)
            handleRequest(HttpMethod.Post, "/quote") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/quote/${quote.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    val responseQuote = Json.decodeFromString<QuoteDTO>(it)
                    assertEquals(responseQuote.id, quote.id)
                    assertEquals(responseQuote.messages[0].message, quote.messages[0].message)
                    assertEquals(responseQuote.authorId, quote.authorId)
                }
            }

            handleRequest(HttpMethod.Delete, "/quote/${quote.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("Quote removed correctly", response.content)
                }
            }
        }
    }

    @Test
    fun `Count quotes test`() {
        val id1 = UUID.randomUUID().toString()
        val id2 = UUID.randomUUID().toString()
        val id3 = UUID.randomUUID().toString()
        val quotes = listOf(
            QuoteDTO(
                id = id1,
                messages = listOf(QuoteLocalizationDTO(code = "test", message = "Test", quoteId = id1)),
                authorId = author.id
            ),
            QuoteDTO(
                id = id2,
                messages = listOf(QuoteLocalizationDTO(code = "test", message = "Test", quoteId = id2)),
                authorId = author.id
            ),
            QuoteDTO(
                id = id3,
                messages = listOf(QuoteLocalizationDTO(code = "test", message = "Test", quoteId = id3)),
                authorId = author.id
            ),
        )
        withTestApplication({ setupTestApplication() }) {
            quotes.forEach { quote ->
                handleRequest(HttpMethod.Post, "/quote") {
                    addHeader("apiKey", "APIKEY")
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(quote.toJson())
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }

            handleRequest(HttpMethod.Get, "/quote/count") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertEquals("{\"count\":3}", response.content)
                }
            }
            quotes.forEach { quote ->
                handleRequest(HttpMethod.Delete, "/quote/${quote.id}") {
                    addHeader("apiKey", "APIKEY")
                }.apply {
                    response.content?.let {
                        assertEquals(HttpStatusCode.Accepted, response.status())
                        assertEquals("Quote removed correctly", response.content)
                    }
                }
            }
        }
    }

    @Test
    fun `List quotes paginated`() {
        val id1 = UUID.randomUUID().toString()
        val id2 = UUID.randomUUID().toString()
        val id3 = UUID.randomUUID().toString()
        val quotes = listOf(
            QuoteDTO(
                id = id1,
                messages = listOf(QuoteLocalizationDTO(code = "test", message = "Test", quoteId = id1)),
                authorId = author.id
            ),
            QuoteDTO(
                id = id2,
                messages = listOf(QuoteLocalizationDTO(code = "test", message = "Test", quoteId = id2)),
                authorId = author.id
            ),
            QuoteDTO(
                id = id3,
                messages = listOf(QuoteLocalizationDTO(code = "test", message = "Test", quoteId = id3)),
                authorId = author.id
            ),
        )
        withTestApplication({ setupTestApplication() }) {
            quotes.forEach { quote ->
                handleRequest(HttpMethod.Post, "/quote") {
                    addHeader("apiKey", "APIKEY")
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(quote.toJson())
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }

            handleRequest(HttpMethod.Get, "/quote?page=1&size=2") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    val response = Json.decodeFromString<PaginatedDTO<List<QuoteDTO>>>(it)
                    assertEquals(2, response.data.size)
                }
            }

            handleRequest(HttpMethod.Get, "/quote?page=1&size=1") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    val response = Json.decodeFromString<PaginatedDTO<List<QuoteDTO>>>(it)
                    assertEquals(1, response.data.size)
                }
            }

            quotes.forEach { quote ->
                handleRequest(HttpMethod.Delete, "/quote/${quote.id}") {
                    addHeader("apiKey", "APIKEY")
                }.apply {
                    response.content?.let {
                        assertEquals(HttpStatusCode.Accepted, response.status())
                        assertEquals("Quote removed correctly", response.content)
                    }
                }
            }
        }
    }

    @Test
    fun `Get random quote empty database`() {
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Get, "/quote/random") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `Get random quote`() {
        withTestApplication({ setupTestApplication() }) {
            val quote = QuoteDTO(id = quoteId, messages = messages, authorId = author.id)
            handleRequest(HttpMethod.Post, "/quote") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/quote/random") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Delete, "/quote/${quote.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("Quote removed correctly", response.content)
                }
            }
        }
    }

    @Test
    fun `Update Quote`() {
        withTestApplication({ setupTestApplication() }) {
            val quote = QuoteDTO(id = quoteId, messages = messages, authorId = author.id)
            handleRequest(HttpMethod.Post, "/quote") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val newQuote = quote.copy(id = quote.id, type = "New type")
            handleRequest(HttpMethod.Put, "/quote") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(newQuote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    val response = Json.decodeFromString<QuoteDTO>(it)
                    assertEquals("New type", response.type)
                }
            }

            handleRequest(HttpMethod.Delete, "/quote/${quote.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("Quote removed correctly", response.content)
                }
            }
        }
    }


    @Test
    fun `Get month quote count`() {
        val id1 = UUID.randomUUID().toString()
        val id2 = UUID.randomUUID().toString()
        val id3 = UUID.randomUUID().toString()
        val oldQuoteId = UUID.randomUUID().toString()
        val quotes = listOf(
            QuoteDTO(
                id = id1,
                messages = listOf(QuoteLocalizationDTO(code = "test", message = "Test", quoteId = id1)),
                authorId = author.id
            ),
            QuoteDTO(
                id = id2,
                messages = listOf(QuoteLocalizationDTO(code = "test", message = "Test", quoteId = id2)),
                authorId = author.id
            ),
            QuoteDTO(
                id = id3,
                messages = listOf(QuoteLocalizationDTO(code = "test", message = "Test", quoteId = id3)),
                authorId = author.id
            ),
        )
        val date = Calendar.getInstance()
        date.add(Calendar.MONTH, -2)
        val oldQuote = QuoteDTO(
            id = oldQuoteId,
            authorId = author.id,
            messages = listOf(QuoteLocalizationDTO(code = "test", message = "Test", quoteId = oldQuoteId)),
            timestamp = date.timeInMillis
        )
        withTestApplication({ setupTestApplication() }) {
            quotes.forEach { quote ->
                handleRequest(HttpMethod.Post, "/quote") {
                    addHeader("apiKey", "APIKEY")
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(quote.toJson())
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }

            handleRequest(HttpMethod.Post, "/quote") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(oldQuote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/quote/month/count") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    assertEquals("{\"count\":3}", response.content)
                }
            }

            quotes.forEach { quote ->
                handleRequest(HttpMethod.Delete, "/quote/${quote.id}") {
                    addHeader("apiKey", "APIKEY")
                }.apply {
                    response.content?.let {
                        assertEquals(HttpStatusCode.Accepted, response.status())
                        assertEquals("Quote removed correctly", response.content)
                    }
                }
            }

            handleRequest(HttpMethod.Delete, "/quote/${oldQuote.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("Quote removed correctly", response.content)
                }
            }
        }
    }

}
