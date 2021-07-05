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
import org.wcode.database.nosql.dao.QueueLocalizationDAO
import org.wcode.database.sql.dao.QuoteLocalizationDAO
import org.wcode.dto.QuoteLocalizationDTO
import org.wcode.dto.ResponseDTO
import org.wcode.interfaces.BaseRoute

class QueueRoutes : BaseRoute, KoinComponent {

    private val queueLocalizationDAO: QueueLocalizationDAO by inject()
    private val quoteLocalizationDAO: QuoteLocalizationDAO by inject()

    override fun setupRouting(routing: Routing) {
        routing {
            route("/queue") {
                header("apiKey", EnvironmentConfig.apiKey) {
                    route("/language") {
                        insertLocalization()
                        authenticate {
                            removeLocalization()
                            applyLocalization()
                            listLocalizations()
                        }
                    }
                }
            }
        }
    }

    private fun Route.listLocalizations() {
        get {
            try {
                call.respond(queueLocalizationDAO.listAll())
            } catch (e: Exception) {
                call.respond(ResponseDTO(success = false, error = e.message))
            }
        }
    }

    private fun Route.insertLocalization() {
        post {
            val quoteLocalization = call.receive<QuoteLocalizationDTO>()
            try {
                queueLocalizationDAO.insert(quoteLocalization)
                call.respond(ResponseDTO())
            } catch (e: Exception) {
                call.respond(ResponseDTO(success = false, error = e.message))
            }
        }
    }

    private fun Route.removeLocalization() {
        delete("{languageId}") {
            val languageId = call.parameters["languageId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            try {
                call.respond(ResponseDTO(success = queueLocalizationDAO.drop(languageId)))
            } catch (e: Exception) {
                call.respond(ResponseDTO(success = false, error = e.message))
            }
        }
    }

    private fun Route.applyLocalization() {
        post("{languageId}") {
            val languageId = call.parameters["languageId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            queueLocalizationDAO.getById(languageId).onSuccess {
                quoteLocalizationDAO.update(it.data).onSuccess { localization ->
                    queueLocalizationDAO.drop(localization.id)
                    call.respond(ResponseDTO())
                }.onFailure { throwable ->
                    call.respond(ResponseDTO(success = false, error = throwable.message))
                }
            }.onFailure {
                call.respond(ResponseDTO(success = false, error = it.message))
            }
        }
    }

}
