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
import org.wcode.database.dao.QuoteDAO
import org.wcode.dto.PaginatedDTO
import org.wcode.dto.QuoteDTO
import org.wcode.interfaces.BaseRoute
import org.wcode.core.EnvironmentConfig

class QuoteRoutes : BaseRoute, KoinComponent {

    private val quoteDao: QuoteDAO by inject()
    private val authorDao: AuthorDAO by inject()

    override fun setupRouting(routing: Routing) {
        routing {
            route("/quote") {
                header("apiKey", EnvironmentConfig.apiKey) {
                    getAll()
                    getPaginated()
                    getById()
                    getRandomQuote()
                    authenticate {
                        deleteQuote()
                        createQuote()
                        updateQuote()
                        countQuotes()
                        montQuoteCount()
                    }
                }
            }
        }
    }

    private fun Route.getAll() {
        get("/all") {
            quoteDao.list(all = true).onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("Failure when retrieving quotes", status = HttpStatusCode.InternalServerError)
            }
        }
    }

    private fun Route.getPaginated() {
        get {
            val query = call.request.queryParameters
            val page = Integer.valueOf(query["page"] ?: "1")
            val size = Integer.valueOf(query["size"] ?: "10")
            val count = quoteDao.count()
            quoteDao.list(page, size).onSuccess {
                val response = PaginatedDTO(it, page, size, call.request.path(), count)
                call.respond(response)
            }.onFailure {
                call.respondText("Failure when retrieving quotes", status = HttpStatusCode.InternalServerError)
            }
        }
    }

    private fun Route.countQuotes() {
        get("count") {
            val count = quoteDao.count()
            call.respond(mapOf("count" to count))
        }
    }

    private fun Route.updateQuote() {
        put {
            val quote = call.receive<QuoteDTO>()
            quoteDao.update(quote).onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("Error when updating quote", status = HttpStatusCode.NotModified)
            }
        }
    }

    private fun Route.getById() {
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            quoteDao.getById(id).onSuccess { quote ->
                authorDao.getById(quote.authorId).onSuccess { author ->
                    call.respond(quote)
                }.onFailure {
                    call.respondText(
                        "No author for quote",
                        status = HttpStatusCode.NotFound
                    )
                }
            }.onFailure {
                call.respondText(
                    "No quote with id $id",
                    status = HttpStatusCode.NotFound
                )
            }
        }
    }

    private fun Route.createQuote() {
        post {
            val quote = call.receive<QuoteDTO>()
            quoteDao.insert(quote).onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("Error when adding quote", status = HttpStatusCode.NotModified)
            }
        }
    }

    private fun Route.deleteQuote() {
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            quoteDao.delete(id).onSuccess {
                call.respondText("Quote removed correctly", status = HttpStatusCode.Accepted)
            }.onFailure {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }

    private fun Route.getRandomQuote() {
        get("random") {
            quoteDao.getRandom().onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }

    private fun Route.montQuoteCount() {
        get("month/count") {
            quoteDao.getMonthCount().onSuccess {
                call.respond(mapOf("count" to it))
            }.onFailure {
                call.respondText("Error in counting quotes of month", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}


