package wcode.software.routes

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import wcode.software.auth.AuthController
import wcode.software.base.BaseRoutes
import wcode.software.data.database.daos.QuoteDAO
import wcode.software.dtos.QuoteDTO
import wcode.software.models.RequestID
import wcode.software.models.Response

class QuoteRoutes : BaseRoutes() {

    private val quoteController = QuoteDAO()
    override val basePath: String = "/quotes"

    override fun addRoutes(javalin: Javalin) {
        javalin.routes {
            path(basePath) {
                get("/", ::getAllQuotes)
                get("/count", ::countQuotes)
                get("/random", ::getRandomQuote)
                get("/month/count", ::getMonthQuotesCount)
                post("/add", ::addQuote)
                delete("/delete", ::deleteQuote)
                put("/update", ::updateQuote)
            }
        }.before(basePath, AuthController::headerDecoderHandler)
    }

    @OpenApi(
        summary = "Get all quotes",
        operationId = "getAllQuotes",
        tags = ["Quotes"],
        responses = [OpenApiResponse("200", [OpenApiContent(Array<QuoteDTO>::class)])]
    )
    private fun getAllQuotes(ctx: Context) {
        ctx.json(quoteController.getAll())
    }

    @OpenApi(
        summary = "Count all quotes",
        operationId = "countQuotes",
        tags = ["Quotes"],
        responses = [OpenApiResponse("200", [OpenApiContent(Int::class)])]
    )
    private fun countQuotes(ctx: Context) {
        ctx.json(quoteController.getCount())
    }

    @OpenApi(
        summary = "Get a random quote",
        operationId = "getRandomQuote",
        tags = ["Quotes"],
        responses = [OpenApiResponse("200", [OpenApiContent(QuoteDTO::class)])]
    )
    private fun getRandomQuote(ctx: Context) {
        ctx.json(quoteController.getRandom())
    }

    @OpenApi(
        summary = "Count month quotes",
        operationId = "getMonthQuotesCount",
        tags = ["Quotes"],
        responses = [OpenApiResponse("200", [OpenApiContent(Int::class)])]
    )
    private fun getMonthQuotesCount(ctx: Context) {
        ctx.json(quoteController.getMonthCount())
    }

    @OpenApi(
        summary = "Add quote to server",
        operationId = "addQuote",
        tags = ["Quotes"],
        requestBody = OpenApiRequestBody([OpenApiContent(QuoteDTO::class)]),
        responses = [OpenApiResponse("200", [OpenApiContent(Response::class)])]
    )
    private fun addQuote(ctx: Context) {
        val response = try {
            val quoteDTO = ctx.body<QuoteDTO>()
            quoteController.insert(quoteDTO)
            Response(
                code = 200,
                message = "Quote successfully added "
            )
        } catch (e: Exception) {
            Response(
                code = 603,
                message = "Error when trying to insert quote"
            )
        }

        ctx.json(response)
    }

    @OpenApi(
        summary = "Delete quote from server",
        operationId = "deleteQuote",
        tags = ["Quotes"],
        requestBody = OpenApiRequestBody([OpenApiContent(RequestID::class)]),
        responses = [OpenApiResponse("200", [OpenApiContent(Response::class)])]
    )
    private fun deleteQuote(ctx: Context) {
        val response = try {
            val requestID = ctx.body<RequestID>()

            quoteController.remove(requestID.id)
            Response(
                code = 200,
                message = "Quote successfully deleted "
            )
        } catch (e: Exception) {
            Response(
                code = 603,
                message = "Error when trying to delete quote"
            )
        }
        ctx.json(response)
    }

    @OpenApi(
        summary = "Update quote in server",
        operationId = "updateQuote",
        tags = ["Quotes"],
        requestBody = OpenApiRequestBody([OpenApiContent(QuoteDTO::class)]),
        responses = [OpenApiResponse("200", [OpenApiContent(Response::class)])]
    )
    private fun updateQuote(ctx: Context) {
        val response = try {
            val quoteDTO = ctx.body<QuoteDTO>()
            quoteController.update(quoteDTO)
            Response(
                code = 200,
                message = "Quote successfully updated "
            )
        } catch (e: Exception) {
            Response(
                code = 603,
                message = "Error when trying to update quote"
            )
        }

        ctx.json(response)
    }
}
