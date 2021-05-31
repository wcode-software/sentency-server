package org.wcode.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.wcode.database.dao.QuoteDAO
import org.wcode.dto.QuoteDTO
import org.wcode.interfaces.BaseRoute

class QuoteRoutes : BaseRoute, KoinComponent {
    private val quoteDao: QuoteDAO by inject()

    override fun setupRouting(routing: Routing) {
        routing {
            route("/quote") {
                get {
                    quoteDao.getAll().onSuccess {
                        call.respond(it)
                    }.onFailure {
                        call.respondText("No quotes found", status = HttpStatusCode.NotFound)
                    }
                }
                get("{id}") {
                    val id = call.parameters["id"] ?: return@get call.respondText(
                        "Missing or malformed id",
                        status = HttpStatusCode.BadRequest
                    )
                    quoteDao.getById(id).onSuccess {
                        call.respond(it)
                    }.onFailure {
                        call.respondText(
                            "No quote with id $id",
                            status = HttpStatusCode.NotFound
                        )
                    }
                }
                post {
                    val quote = call.receive<QuoteDTO>()
                    quoteDao.insert(quote).onSuccess {
                        call.respond(it)
                    }.onFailure {
                        call.respondText("Error when adding quote", status = HttpStatusCode.NotModified)
                    }

                }
                delete("{id}") {
                    val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                    quoteDao.delete(id).onSuccess {
                        call.respondText("Quote removed correctly", status = HttpStatusCode.Accepted)
                    }.onFailure {
                        call.respondText("Not Found", status = HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }
}


