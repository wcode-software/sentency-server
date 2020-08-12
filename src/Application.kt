package wcode.software

import io.javalin.Javalin
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Info
import wcode.software.database.DatabaseFactory
import wcode.software.routes.AuthorRoutes
import wcode.software.routes.QuoteRoutes

fun main() {

    DatabaseFactory.init()

    val app = Javalin.create {config ->
        config.enableCorsForAllOrigins()
        config.registerPlugin(getConfiguredOpenApiPlugin())
        config.defaultContentType = "application/json"
        config.enableDevLogging()
    }.start()

    AuthorRoutes.addRoutes(app)
    QuoteRoutes.addRoutes(app)
}

fun getConfiguredOpenApiPlugin() = OpenApiPlugin(
    OpenApiOptions(
        Info().apply {
            version("1.0")
            description("Sentency API")
        }
    ).apply {
        path("/openapi")
        swagger(SwaggerOptions("/docs"))
        reDoc(ReDocOptions("/redoc"))
    }
)