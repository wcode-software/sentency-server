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
}
