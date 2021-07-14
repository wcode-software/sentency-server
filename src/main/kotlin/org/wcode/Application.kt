package org.wcode

import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.wcode.plugins.*

fun main() {
    embeddedServer(CIO, port = 7000, host = "0.0.0.0") {
        configureSettings()
        configureDependencyInjection()
        configureAuth()
        configureRouting()
        configureHTTP()
    }.start(wait = true)
}
