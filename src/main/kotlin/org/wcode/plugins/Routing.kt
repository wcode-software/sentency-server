package org.wcode.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*
import org.wcode.routes.*

fun Application.configureRouting() {
    val routes = listOf(AuthorRoutes(), QuoteRoutes(), UserRoutes(), QuoteLocalizationRoutes(), QueueRoutes())
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        routes.forEach { route ->
            route.setupRouting(this)
        }
    }
}
