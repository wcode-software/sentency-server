package org.wcode.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.wcode.database.dao.AuthorDAO
import org.wcode.dto.AuthorDTO
import org.wcode.dto.PaginatedDTO
import org.wcode.interfaces.BaseRoute
import org.wcode.core.EnvironmentConfig

class AuthorRoutes : BaseRoute, KoinComponent {

    private val authorDAO: AuthorDAO by inject()

    override fun setupRouting(routing: Routing) {
        routing {
            route("/author") {
                header("apiKey", EnvironmentConfig.apiKey) {
                    getAll()
                    getPaginated()
                    getById()
                    getAuthorQuotes()
                    authenticate {
                        createAuthor()
                        updateAuthor()
                        deleteAuthor()
                        getTopAuthor()
                        countAuthors()
                    }
                }
            }
        }
    }

    private fun Route.getAll() {
        get("all") {
            authorDAO.list(all = true).onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText(
                    "Failure when retrieving authors",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }
    }

    private fun Route.getPaginated() {
        get {
            val query = call.request.queryParameters
            val page = Integer.valueOf(query["page"] ?: "1")
            val size = Integer.valueOf(query["size"] ?: "10")
            val count = authorDAO.count()
            authorDAO.list(page, size).onSuccess {
                val response = PaginatedDTO(it, page, size, call.request.path(), count)
                call.respond(response)
            }.onFailure {
                call.respondText(
                    "Failure when retrieving authors",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }
    }

    private fun Route.getById() {
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
    }

    private fun Route.createAuthor() {
        post {
            val author = call.receive<AuthorDTO>()
            authorDAO.insert(author).onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("Error when adding author", status = HttpStatusCode.NotModified)
            }
        }
    }

    private fun Route.countAuthors() {
        get("count") {
            val count = authorDAO.count()
            call.respond(mapOf("count" to count))
        }
    }

    private fun Route.updateAuthor() {
        put {
            val author = call.receive<AuthorDTO>()
            authorDAO.update(author).onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("Error when updating author", status = HttpStatusCode.NotModified)
            }
        }
    }

    private fun Route.deleteAuthor() {
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            authorDAO.delete(id).onSuccess {
                call.respondText("Author removed correctly", status = HttpStatusCode.Accepted)
            }.onFailure {
                call.respondText("Author not Found", status = HttpStatusCode.NotFound)
            }
        }
    }

    private fun Route.getAuthorQuotes() {
        get("{id}/quotes") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            authorDAO.getAllAuthorQuotes(id).onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("Author not Found", status = HttpStatusCode.NotFound)
            }
        }
    }

    private fun Route.getTopAuthor() {
        get("/top") {
            authorDAO.getAuthorWithMostQuotes().onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("Author not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}

