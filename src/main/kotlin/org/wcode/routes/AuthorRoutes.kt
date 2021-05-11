package org.wcode.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import org.wcode.database.dao.AuthorDAO
import org.wcode.dto.AuthorDTO

fun Route.authorRouting() {

    val authorDAO: AuthorDAO by inject()

    route("/author") {
        get {
            authorDAO.getAll().onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("No authors found", status = HttpStatusCode.NotFound)
            }
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id", status = HttpStatusCode.BadRequest
            )
            authorDAO.getById(id).onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText(
                    "No author with id $id",
                    status = HttpStatusCode.NotFound
                )
            }
        }
        post {
            val author = call.receive<AuthorDTO>()
            authorDAO.insert(author).onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("Error when adding author", status = HttpStatusCode.NotModified)
            }
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            authorDAO.delete(id).onSuccess {
                call.respondText("Author removed correctly", status = HttpStatusCode.Accepted)
            }.onFailure {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
