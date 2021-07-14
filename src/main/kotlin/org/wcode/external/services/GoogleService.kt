package org.wcode.external.services

import com.google.gson.JsonObject
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.features.*
import org.wcode.core.EnvironmentConfig
import org.wcode.database.nosql.dao.TranslationDAO
import org.wcode.database.nosql.models.TranslationMongo
import org.wcode.database.sql.dao.QuoteDAO
import org.wcode.external.models.ReCaptchaDTO
import org.wcode.external.requests.TranslateRequest

class GoogleService(
    private val translateDAO: TranslationDAO,
    private val quoteDAO: QuoteDAO
) {

    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    suspend fun checkRecaptcha(reCaptchaDTO: ReCaptchaDTO): Boolean {
        return try {
            val response: ReCaptchaResponse = client.post("https://www.google.com/recaptcha/api/siteverify") {
                parameter("secret", EnvironmentConfig.reCaptchaKey)
                parameter("response", reCaptchaDTO.token)
            }

            response.success && response.action == reCaptchaDTO.action && response.score > 0.5
        } catch (e: Exception) {
            print(e.localizedMessage)
            false
        }
    }

    private fun getFromCacheDatabase(quoteId: String, code: String): String? {
        val translation = translateDAO.getByCodeId(code, quoteId).getOrNull()
        return translation?.message
    }

    private suspend fun getFromGCP(code: String, message: String): String? {
        val url = "https://translation.googleapis.com/language/translate/v2?key=${EnvironmentConfig.gcpAPIKey}"
        val request = TranslateRequest(message = message, code = code)
        return try {
            val response: JsonObject = client.post(url) {
                headers {
                    append("Content-Type", "application/json")
                }
                body = request
            }
            val translation = response.getAsJsonObject("data")
                .getAsJsonArray("translations")[0]
                .asJsonObject
            translation.get("translatedText").asString
        } catch (e: Exception) {
            print(e.localizedMessage)
            null
        }
    }

    private fun getDefaultQuote(quoteId: String): String? {
        val quote = quoteDAO.getById(quoteId).getOrNull()
        return quote?.let { quoteDTO ->
            val defaultMessage = quoteDTO.messages.find { it.code == "en-US" }
            defaultMessage?.message
        }
    }

    suspend fun translate(target: String, quoteId: String): Result<String> {
        val existingTranslation = getFromCacheDatabase(quoteId, target)
        if (existingTranslation != null) {
            return Result.success(existingTranslation)
        }

        val defaultQuote = getDefaultQuote(quoteId) ?: return Result.failure(NotFoundException())
        val translatedQuote = getFromGCP(target, defaultQuote)

        return translatedQuote?.let {
            translateDAO.insert(TranslationMongo(quoteId = quoteId, message = it, code = target))
            Result.success(it)
        } ?: Result.failure(InternalError())

    }

}
