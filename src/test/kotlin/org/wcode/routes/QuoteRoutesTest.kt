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
import java.util.*
import kotlin.test.assertEquals

class QuoteRoutesTest {

    @Test
    fun `Add Quote with ID`() {
        val author = AuthorDTO(id = UUID.randomUUID().toString(), name = "Test")
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/author") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(author.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val quote = QuoteDTO(id = UUID.randomUUID().toString(), message = "Test", authorId = author.id)
            handleRequest(HttpMethod.Post, "/quote") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `Add Quote without ID`() {
        val author = AuthorDTO(id = UUID.randomUUID().toString(), name = "Test")
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/author") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(author.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val quote = QuoteDTO(message = "Test", authorId = author.id)
            handleRequest(HttpMethod.Post, "/quote") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `Get all Quotes empty list`() {
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Get, "/quote/").apply {
                response.content?.let {
                    val responseQuote = Json.decodeFromString<List<QuoteDTO>>(it)
                    assertEquals(responseQuote.size, 0)
                }
            }
        }
    }

    @Test
    fun `Get all Quotes list with one quote`() {
        val author = AuthorDTO(id = UUID.randomUUID().toString(), name = "Test")
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/author") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(author.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val quote = QuoteDTO(id = UUID.randomUUID().toString(), message = "Test", authorId = author.id)
            handleRequest(HttpMethod.Post, "/quote") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/author/").apply {
                response.content?.let {
                    val responseQuote = Json.decodeFromString<List<QuoteDTO>>(it)
                    assertEquals(responseQuote.size, 1)
                }
            }
        }
    }

    @Test
    fun `Get Quote by ID`() {
        val author = AuthorDTO(id = UUID.randomUUID().toString(), name = "Test")
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/author") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(author.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val quote = QuoteDTO(id = UUID.randomUUID().toString(), message = "Test", authorId = author.id)
            handleRequest(HttpMethod.Post, "/quote") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/quote/${quote.id}").apply {
                response.content?.let {
                    val responseQuote = Json.decodeFromString<QuoteDTO>(it)
                    assertEquals(responseQuote.id, quote.id)
                    assertEquals(responseQuote.message, quote.message)
                    assertEquals(responseQuote.authorId, quote.authorId)
                }
            }
        }
    }

    @Test
    fun `Delete Quote by ID`() {
        val author = AuthorDTO(id = UUID.randomUUID().toString(), name = "Test")
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/author") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(author.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val quote = QuoteDTO(id = UUID.randomUUID().toString(), message = "Test", authorId = author.id)
            handleRequest(HttpMethod.Post, "/quote") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/quote/${quote.id}").apply {
                response.content?.let {
                    val responseQuote = Json.decodeFromString<QuoteDTO>(it)
                    assertEquals(responseQuote.id, quote.id)
                    assertEquals(responseQuote.message, quote.message)
                    assertEquals(responseQuote.authorId, quote.authorId)
                }
            }

            handleRequest(HttpMethod.Delete, "/quote/${quote.id}").apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("Quote removed correctly", response.content)
                }
            }
        }
    }
}