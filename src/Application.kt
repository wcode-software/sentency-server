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
import wcode.software.routes.UserRoutes

fun main() {

    loadEnvVariables()
    DatabaseFactory.init()

    val app = Javalin.create { config ->
        config.enableCorsForAllOrigins()
        config.registerPlugin(getConfiguredOpenApiPlugin())
        config.defaultContentType = "application/json"
        config.enableDevLogging()
    }.start()

    val routesList = listOf(AuthorRoutes, QuoteRoutes, UserRoutes)

    routesList.forEach { route ->
        route.addRoutes(app)
    }
}

fun loadEnvVariables() {
    val dotenv = dotenv {
        directory = "./resources"
        filename = "development.env"
    }
    Environment.startEnvironment(dotenv)
}

fun getConfiguredOpenApiPlugin() = OpenApiPlugin(
    OpenApiOptions(
        Info().apply {
            version("0.2.0")
            description("Sentency API")
        }
    ).apply {
        path("/openapi")
        swagger(SwaggerOptions("/docs"))
        reDoc(ReDocOptions("/redoc"))
    }
)