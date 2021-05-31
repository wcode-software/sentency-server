package org.wcode.routes

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.wcode.core.setupTestApplication
import org.wcode.dto.AuthorDTO
import java.util.*
import kotlin.test.assertEquals

class AuthorRoutesTest {

    @Test
    fun `Add Author with ID`() {
        val author = AuthorDTO(id = UUID.randomUUID().toString(), name = "Test")
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/author") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(author.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `Add Author without ID`() {
        val author = AuthorDTO(name = "Test")
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/author") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(author.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `Get all Authors empty list`() {
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Get, "/author/").apply {
                response.content?.let {
                    val responseAuthor = Json.decodeFromString<List<AuthorDTO>>(it)
                    assertEquals(responseAuthor.size, 0)
                }
            }
        }
    }

    @Test
    fun `Get all Authors list with one author`() {
        val author = AuthorDTO(id = UUID.randomUUID().toString(), name = "Test")
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/author") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(author.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/author/").apply {
                response.content?.let {
                    val responseAuthor = Json.decodeFromString<List<AuthorDTO>>(it)
                    assertEquals(responseAuthor.size, 1)
                }
            }
        }
    }

    @Test
    fun `Get Author by ID`() {
        val author = AuthorDTO(id = UUID.randomUUID().toString(), name = "Test")
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/author") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(author.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/author/${author.id}").apply {
                response.content?.let {
                    val responseAuthor = Json.decodeFromString<AuthorDTO>(it)
                    assertEquals(author.id, responseAuthor.id)
                    assertEquals(author.name, responseAuthor.name)
                    assertEquals(author.picUrl, responseAuthor.picUrl)
                }
            }
        }
    }

    @Test
    fun `Delete Author by ID`() {
        val author = AuthorDTO(id = UUID.randomUUID().toString(), name = "Test")
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/author") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(author.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/author/${author.id}").apply {
                response.content?.let {
                    val responseAuthor = Json.decodeFromString<AuthorDTO>(it)
                    assertEquals(author.id, responseAuthor.id)
                    assertEquals(author.name, responseAuthor.name)
                    assertEquals(author.picUrl, responseAuthor.picUrl)
                }
            }

            handleRequest(HttpMethod.Delete, "/author/${author.id}").apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("Author removed correctly", response.content)
                }
            }
        }
    }
}
