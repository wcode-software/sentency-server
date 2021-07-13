package org.wcode.external.services

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import org.wcode.core.EnvironmentConfig
import org.wcode.external.models.ReCaptchaDTO

class GoogleService {

    private val client = HttpClient(CIO){
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun checkRecaptcha(reCaptchaDTO: ReCaptchaDTO): Boolean {
        return try {
            val response : ReCaptchaResponse = client.post("https://www.google.com/recaptcha/api/siteverify") {
                parameter("secret", EnvironmentConfig.reCaptchaKey)
                parameter("response", reCaptchaDTO.token)
            }

            response.success && response.action == reCaptchaDTO.action && response.score > 0.5
        } catch (e: Exception) {
            print(e.localizedMessage)
            false
        }
    }
}
