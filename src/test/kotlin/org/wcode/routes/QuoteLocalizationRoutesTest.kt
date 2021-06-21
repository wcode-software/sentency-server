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
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class QuoteLocalizationRoutesTest {

    val author = AuthorDTO(name = "Walter")
    val quote = QuoteDTO(authorId = author.id)

    @Before
    fun initTest() {
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/author") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("apiKey", "APIKEY")
                setBody(author.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Post, "/quote") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @After
    fun cleanTest() {
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Delete, "/quote/${quote.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("Quote removed correctly", response.content)
                }
            }

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
    fun `Create quote localization`() {
        withTestApplication({ setupTestApplication() }) {
            val quoteLocalization = QuoteLocalizationDTO(code = "Te", quoteId = quote.id, message = "Localization test")
            handleRequest(HttpMethod.Post, "/language") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quoteLocalization.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Delete, "/language/${quoteLocalization.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                assertEquals(HttpStatusCode.Accepted, response.status())
            }
        }
    }

    @Test
    fun `Update quote localization`() {
        withTestApplication({ setupTestApplication() }) {
            val quoteLocalization = QuoteLocalizationDTO(code = "Te", quoteId = quote.id, message = "Localization test")
            handleRequest(HttpMethod.Post, "/language") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quoteLocalization.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val newLocalization = quoteLocalization.copy(id = quoteLocalization.id, message = "New message")
            handleRequest(HttpMethod.Put, "/language") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(newLocalization.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    val localization = Json.decodeFromString<QuoteLocalizationDTO>(it)
                    assertEquals(newLocalization.message, localization.message)
                }
            }

            handleRequest(HttpMethod.Delete, "/language/${quoteLocalization.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                assertEquals(HttpStatusCode.Accepted, response.status())
            }
        }
    }

    @Test
    fun `Count quote localization`() {
        withTestApplication({ setupTestApplication() }) {
            val quoteLocalizations = listOf(
                QuoteLocalizationDTO(code = "Te", quoteId = quote.id, message = "Localization test"),
                QuoteLocalizationDTO(code = "Te2", quoteId = quote.id, message = "Localization test"),
                QuoteLocalizationDTO(code = "Te3", quoteId = quote.id, message = "Localization test")
            )
            quoteLocalizations.forEach { quoteLocalization ->
                handleRequest(HttpMethod.Post, "/language") {
                    addHeader("apiKey", "APIKEY")
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(quoteLocalization.toJson())
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }

            handleRequest(HttpMethod.Get, "/language/count/${quote.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertEquals("{\"count\":3}", response.content)
                }
            }

            quoteLocalizations.forEach { quoteLocalization ->
                handleRequest(HttpMethod.Delete, "/language/${quoteLocalization.id}") {
                    addHeader("apiKey", "APIKEY")
                }.apply {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                }
            }
        }
    }

    @Test
    fun `Get all quote localizations`() {
        withTestApplication({ setupTestApplication() }) {
            val quoteLocalizations = listOf(
                QuoteLocalizationDTO(code = "Te", quoteId = quote.id, message = "Localization test"),
                QuoteLocalizationDTO(code = "Te2", quoteId = quote.id, message = "Localization test"),
                QuoteLocalizationDTO(code = "Te3", quoteId = quote.id, message = "Localization test")
            )
            quoteLocalizations.forEach { quoteLocalization ->
                handleRequest(HttpMethod.Post, "/language") {
                    addHeader("apiKey", "APIKEY")
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(quoteLocalization.toJson())
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }

            handleRequest(HttpMethod.Get, "/language/${quote.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.OK, response.status())
                    response.content?.let {
                        val languages = Json.decodeFromString<List<QuoteLocalizationDTO>>(it)
                        assertEquals(3, languages.size)
                    }
                }
            }

            quoteLocalizations.forEach { quoteLocalization ->
                handleRequest(HttpMethod.Delete, "/language/${quoteLocalization.id}") {
                    addHeader("apiKey", "APIKEY")
                }.apply {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                }
            }
        }
    }
}
