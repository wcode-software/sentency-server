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
}
