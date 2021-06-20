package org.wcode.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*
import org.wcode.routes.AuthorRoutes
import org.wcode.routes.QuoteLocalizationRoutes
import org.wcode.routes.QuoteRoutes
import org.wcode.routes.UserRoutes

fun Application.configureRouting() {
    val routes = listOf(AuthorRoutes(), QuoteRoutes(), UserRoutes(), QuoteLocalizationRoutes())
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        routes.forEach { route ->
            route.setupRouting(this)
        }
    }
}
