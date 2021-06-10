package org.wcode

import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*
import org.wcode.core.setupTestApplication
import org.wcode.plugins.configureRouting

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ setupTestApplication() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello World!", response.content)
            }
        }
    }
}
