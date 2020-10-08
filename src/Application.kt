package wcode.software

import io.github.cdimascio.dotenv.dotenv
import io.javalin.Javalin
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Info
import wcode.software.config.Environment
import wcode.software.database.DatabaseFactory
import wcode.software.routes.AuthorRoutes
import wcode.software.routes.QuoteRoutes

fun main() {

    loadEnvVariables()
    DatabaseFactory.init()

    val app = Javalin.create { config ->
        config.enableCorsForAllOrigins()
        config.registerPlugin(getConfiguredOpenApiPlugin())
        config.defaultContentType = "application/json"
        config.enableDevLogging()
    }.start()

    AuthorRoutes.addRoutes(app)
    QuoteRoutes.addRoutes(app)
}

fun loadEnvVariables() {
    val dotenv = dotenv {
        directory = "./resources"
        filename = "production.env"
    }
    Environment.startEnvironment(dotenv)
}

fun getConfiguredOpenApiPlugin() = OpenApiPlugin(
    OpenApiOptions(
        Info().apply {
            version("0.1.1")
            description("Sentency API")
        }
    ).apply {
        path("/openapi")
        swagger(SwaggerOptions("/docs"))
        reDoc(ReDocOptions("/redoc"))
    }
)