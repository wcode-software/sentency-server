package org.wcode.routes

import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import org.wcode.models.Quote
import org.wcode.setupApplication
import kotlin.test.assertEquals

class QuoteRoutesTest {

    @Test
    fun `Add Customer with ID`() {
        val quote = Quote(id = "Teste", message = "Test", authorId = "Test")
        withTestApplication({ setupApplication() }) {
            handleRequest(HttpMethod.Post, "/quotes") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())
                assertEquals("Quote stored correctly", response.content)
            }
        }
    }

    @Test
    fun `Add Customer without ID`() {
        val quote = Quote( message = "Test", authorId = "Test")
        withTestApplication({ setupApplication() }) {
            handleRequest(HttpMethod.Post, "/quotes") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(quote.toJson())
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())
                assertEquals("Quote stored correctly", response.content)
            }
        }
    }
}
