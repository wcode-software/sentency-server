package org.wcode.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.wcode.models.Quote
import org.wcode.models.quoteStorage
import java.util.*

fun Route.quoteRouting(){
    route("/quotes") {
        get {
            if (quoteStorage.isNotEmpty()) {
                call.respond(quoteStorage)
            } else {
                call.respondText("No quotes found", status = HttpStatusCode.NotFound)
            }
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val quote =
                quoteStorage.find { it.id == id } ?: return@get call.respondText(
                    "No quote with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(quote)
        }
        post {
            val quote = call.receive<Quote>()
            quoteStorage.add(quote)
            call.respondText("Quote stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (quoteStorage.removeIf { it.id == id }) {
                call.respondText("Quote removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
