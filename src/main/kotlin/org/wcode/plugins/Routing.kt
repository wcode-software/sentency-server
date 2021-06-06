package org.wcode.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import org.wcode.routes.AuthorRoutes
import org.wcode.routes.QuoteRoutes
import org.wcode.routes.UserRoutes

fun Application.configureRouting() {
    val routes = listOf(AuthorRoutes(), QuoteRoutes(), UserRoutes())
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/anything"){
            val received = call.receiveText()
            print(received)
        }
        routes.forEach { route ->
            route.setupRouting(this)
        }
    }
}
