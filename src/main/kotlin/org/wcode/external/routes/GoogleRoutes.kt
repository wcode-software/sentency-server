package org.wcode.external.routes

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.wcode.core.EnvironmentConfig
import org.wcode.external.models.ReCaptchaDTO
import org.wcode.dto.ResponseDTO
import org.wcode.interfaces.BaseRoute
import org.wcode.external.services.GoogleService

class GoogleRoutes : BaseRoute, KoinComponent {

    private val googleService: GoogleService by inject()

    override fun setupRouting(routing: Routing) {
        routing {
            header("apiKey", EnvironmentConfig.apiKey) {
                route("/external") {
                    route("/google") {
                        recaptchaCheck()
                    }
                }
            }
        }
    }

    private fun Route.recaptchaCheck() {
        post("reCaptcha") {
            val reCaptchaDTO = call.receive<ReCaptchaDTO>()
            val resultRecaptcha = googleService.checkRecaptcha(reCaptchaDTO)
            call.respond(ResponseDTO(resultRecaptcha))
        }
    }
}
