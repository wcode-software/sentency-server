package models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import wcode.software.models.RequestID

class RequestIDTest {

    @Test
    fun testRequestIDCreation() {
        val requestID = RequestID(id = "idTest")
        assertEquals("idTest", requestID.id)
    }
}