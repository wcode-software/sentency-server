package org.wcode.routes

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.wcode.core.setupTestApplication
import org.wcode.dto.AuthDTO
import org.wcode.dto.LoginUserDTO
import org.wcode.dto.PaginatedDTO
import org.wcode.dto.UserDTO
import kotlin.test.assertEquals

class UserRoutesTest {

    @Test
    fun `Create user`() {
        val userDTO = UserDTO(email = "walter@test", password = "Test")
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/user") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("apiKey", "APIKEY")
                setBody(userDTO.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Delete, "/user/${userDTO.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("User removed correctly", response.content)
                }
            }
        }
    }

    @Test
    fun `List all users`() {
        val userDTO = UserDTO(email = "walter@test", password = "Test")
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/user") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("apiKey", "APIKEY")
                setBody(userDTO.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/user/all") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    val users = Json.decodeFromString<List<UserDTO>>(it)
                    assertEquals(1, users.size)
                }
            }

            handleRequest(HttpMethod.Delete, "/user/${userDTO.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("User removed correctly", response.content)
                }
            }
        }
    }

    @Test
    fun `Login user that exist`() {
        val userDTO = UserDTO(email = "walter@test", password = "Test")
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Post, "/user") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("apiKey", "APIKEY")
                setBody(userDTO.toJson())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val loginDTO = LoginUserDTO(userDTO.email, userDTO.password)
            handleRequest(HttpMethod.Post, "/user/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("apiKey", "APIKEY")
                setBody(loginDTO.toJson())
            }.apply {
                response.content?.let {
                    val auth = Json.decodeFromString<AuthDTO>(it)
                    assert(auth.token.isNotEmpty())
                }
            }

            handleRequest(HttpMethod.Delete, "/user/${userDTO.id}") {
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    assertEquals(HttpStatusCode.Accepted, response.status())
                    assertEquals("User removed correctly", response.content)
                }
            }
        }
    }

    @Test
    fun `Login user that doesn't exist`() {
        withTestApplication({ setupTestApplication() }) {
            val loginDTO = LoginUserDTO("Test", "password")
            handleRequest(HttpMethod.Post, "/user/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("apiKey", "APIKEY")
                setBody(loginDTO.toJson())
            }.apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `Get created users paginated`() {
        val usersDTO = listOf(
            UserDTO(email = "walter@test", password = "Test"),
            UserDTO(email = "walter1@test", password = "Test"),
            UserDTO(email = "walter2@test", password = "Test")
        )
        withTestApplication({ setupTestApplication() }) {
            usersDTO.forEach { userDTO ->
                handleRequest(HttpMethod.Post, "/user") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    addHeader("apiKey", "APIKEY")
                    setBody(userDTO.toJson())
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }

            handleRequest(HttpMethod.Get, "/user?page=1&size=1") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    val users = Json.decodeFromString<PaginatedDTO<List<UserDTO>>>(it)
                    assertEquals(1, users.data.size)
                }
            }


            handleRequest(HttpMethod.Get, "/user?page=1&size=2") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("apiKey", "APIKEY")
            }.apply {
                response.content?.let {
                    val users = Json.decodeFromString<PaginatedDTO<List<UserDTO>>>(it)
                    assertEquals(2, users.data.size)
                }
            }

            usersDTO.forEach { userDTO ->
                handleRequest(HttpMethod.Delete, "/user/${userDTO.id}") {
                    addHeader("apiKey", "APIKEY")
                }.apply {
                    response.content?.let {
                        assertEquals(HttpStatusCode.Accepted, response.status())
                        assertEquals("User removed correctly", response.content)
                    }
                }
            }
        }
    }
}
