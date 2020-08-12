package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import wcode.software.models.Response

class ResponseTest {

    @Test
    fun testResponseCreationWithMessage() {
        val response = Response(code = 1, message = "not null")
        assertEquals(1, response.code)
        assertEquals("not null", response.message)
    }

    @Test
    fun testResponseCreationWithoutMessage() {
        val response = Response(code = 1)
        assertEquals(1, response.code)
        assertEquals(null, response.message)
    }
}