package org.wcode.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.wcode.core.EnvironmentConfig
import org.wcode.database.dao.QuoteLocalizationDAO
import org.wcode.dto.QuoteLocalizationDTO
import org.wcode.interfaces.BaseRoute

class QuoteLocalizationRoutes : BaseRoute, KoinComponent {

    private val quoteLocalizationDAO: QuoteLocalizationDAO by inject()

    override fun setupRouting(routing: Routing) {
        routing {
            route("/language") {
                header("apiKey", EnvironmentConfig.apiKey) {
                    getAll()
                    authenticate {
                        deleteQuoteLocalization()
                        createQuoteLocalization()
                        updateQuoteLocalization()
                        countQuotesLocalization()
                    }
                }
            }
        }
    }

    private fun Route.getAll() {
        get("{quote_id") {
            val id = call.parameters["quote_id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            quoteLocalizationDAO.getAllQuoteLocalizations(id).onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("Error when getting localizations", status = HttpStatusCode.NotFound)
            }
        }
    }

    private fun Route.createQuoteLocalization() {
        post {
            val quoteLocalization = call.receive<QuoteLocalizationDTO>()
            quoteLocalizationDAO.insert(quoteLocalization).onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("Error when creating localization for quote", status = HttpStatusCode.NotFound)
            }
        }
    }

    private fun Route.updateQuoteLocalization() {
        put {
            val quoteLocalization = call.receive<QuoteLocalizationDTO>()
            quoteLocalizationDAO.update(quoteLocalization).onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("Error when updating localization for quote", status = HttpStatusCode.NotFound)
            }
        }
    }

    private fun Route.countQuotesLocalization() {
        get("count/{quote_id}") {
            val quoteId = call.parameters["quote_id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val count = quoteLocalizationDAO.countQuoteLocalizations(quoteId)
            call.respond(mapOf("count" to count))
        }
    }


    private fun Route.deleteQuoteLocalization() {
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            quoteLocalizationDAO.delete(id).onSuccess {
                call.respondText("Quote Localization removed correctly", status = HttpStatusCode.Accepted)
            }.onFailure {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }

}
