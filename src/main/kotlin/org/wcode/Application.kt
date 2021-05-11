package org.wcode

import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import org.wcode.plugins.*

fun main() {
    embeddedServer(Jetty, port = 8080, host = "0.0.0.0") {
        configureModules()
        configureRouting()
        configureHTTP()
    }.start(wait = true)
}
