package org.wcode

import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import org.wcode.plugins.*

fun main() {
    embeddedServer(Jetty, port = 7000, host = "0.0.0.0") {
        configureSettings()
        configureDependencyInjection()
        configureRouting()
        configureHTTP()
    }.start(wait = true)
}
