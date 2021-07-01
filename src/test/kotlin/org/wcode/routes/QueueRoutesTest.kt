package org.wcode.routes

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.wcode.core.setupTestApplication
import org.wcode.dto.*
import java.util.*
import kotlin.test.assertEquals

class QueueRoutesTest {

    private val author = AuthorDTO(name = "Walter")
    private val quoteId = UUID.randomUUID().toString()
    private val message = QuoteLocalizationDTO(message = "Test", code = "test", quoteId = quoteId)
    private val quote = QuoteDTO(id = quoteId, authorId = author.id, messages = listOf(message))

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
    fun `Add quote localization to queue`() {
        withTestApplication({ setupTestApplication() }) {
            val quoteLocalization =
                QuoteLocalizationDTO(code = "Te", quoteId = "quoteId", message = "Localization test")
            handleRequest(HttpMethod.Post, "/queue/language") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quoteLocalization.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    val response = Json.decodeFromString<ResponseDTO>(it)
                    assertEquals(true, response.success)
                }
            }

            handleRequest(HttpMethod.Delete, "/queue/language/${quoteLocalization.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `Delete localization to queue`() {
        withTestApplication({ setupTestApplication() }) {
            val quoteLocalization =
                QuoteLocalizationDTO(code = "Te", quoteId = "quoteId", message = "Localization test")
            handleRequest(HttpMethod.Post, "/queue/language") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quoteLocalization.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Delete, "/queue/language/${quoteLocalization.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    val response = Json.decodeFromString<ResponseDTO>(it)
                    assertEquals(true, response.success)
                }
            }
        }
    }

    @Test
    fun `Delete localization that don't exist`() {
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Delete, "/queue/language/Test") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    val response = Json.decodeFromString<ResponseDTO>(it)
                    assertEquals(false, response.success)
                }
            }
        }
    }

    @Test
    fun `Delete localization missing parameter`() {
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Delete, "/queue/language") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `List queue quote localization`() {
        val quoteLocalizations = listOf(
            QuoteLocalizationDTO(code = "Te", quoteId = "quoteId", message = "Localization test"),
            QuoteLocalizationDTO(code = "Te2", quoteId = "quoteId", message = "Localization test"),
            QuoteLocalizationDTO(code = "Te3", quoteId = "quoteId", message = "Localization test")
        )
        withTestApplication({ setupTestApplication() }) {
            quoteLocalizations.forEach { quoteLocalization ->
                handleRequest(HttpMethod.Post, "/queue/language") {
                    addHeader("apiKey", "APIKEY")
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(quoteLocalization.toJson())
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }

            handleRequest(HttpMethod.Get, "/queue/language") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    val response = Json.decodeFromString<List<QueueDTO<QuoteLocalizationDTO>>>(it)
                    assertEquals(3, response.size)
                }
            }

            quoteLocalizations.forEach { quoteLocalization ->
                handleRequest(HttpMethod.Delete, "/queue/language/${quoteLocalization.id}") {
                    addHeader("apiKey", "APIKEY")
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    response.content?.let {
                        val response = Json.decodeFromString<ResponseDTO>(it)
                        assertEquals(true, response.success)
                    }
                }
            }
        }

    }

    @Test
    fun `Apply localization`() {
        withTestApplication({ setupTestApplication() }) {
            val quoteLocalization = message.copy(id = message.id, message = "New message")
            handleRequest(HttpMethod.Post, "/queue/language") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quoteLocalization.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    val response = Json.decodeFromString<ResponseDTO>(it)
                    assertEquals(true, response.success)
                }
            }

            var queueId: String = ""
            handleRequest(HttpMethod.Get, "/queue/language") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    val response = Json.decodeFromString<List<QueueDTO<QuoteLocalizationDTO>>>(it)
                    assertEquals(1, response.size)
                    queueId = response[0].id


                }
            }

            handleRequest(HttpMethod.Post, "/queue/language/$queueId") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/queue/language") {
                addHeader("apiKey", "APIKEY")
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content?.let {
                    val response = Json.decodeFromString<List<QueueDTO<QuoteLocalizationDTO>>>(it)
                    assertEquals(0, response.size)
                }
            }
        }
    }
}
