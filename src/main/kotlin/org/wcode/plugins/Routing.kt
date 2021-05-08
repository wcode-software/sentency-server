package org.wcode.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*
import org.wcode.routes.quoteRouting

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        quoteRouting()
    }
}
