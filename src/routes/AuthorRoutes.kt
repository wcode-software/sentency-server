package wcode.software.routes

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import wcode.software.base.BaseRoutes
import wcode.software.database.controllers.AuthorDAO
import wcode.software.dtos.AuthorDTO
import wcode.software.dtos.QuoteDTO
import wcode.software.models.RequestID
import wcode.software.models.Response

object AuthorRoutes : BaseRoutes {

    private val authorController = AuthorDAO()
    private val basePath: String = "/authors"

    override fun addRoutes(javalin: Javalin) {
        javalin.routes {
            path(basePath) {
                get("/", ::getAllAuthors)
                get("/count", ::countAuthors)
                post("/quotes/", ::getAuthorQuotes)
                get("/top", ::getTopAuthor)
                post("/add", ::addAuthor)
                delete("/delete", ::deleteAuthor)
                put("/update", ::updateAuthor)
            }
        }
    }

    @OpenApi(
        summary = "Get all authors",
        operationId = "getAllAuthors",
        tags = ["Authors"],
        responses = [OpenApiResponse("200", [OpenApiContent(Array<AuthorDTO>::class)])]
    )
    private fun getAllAuthors(ctx: Context) {
        ctx.json(authorController.getAll())
    }

    @OpenApi(
        summary = "Count all registered authors",
        operationId = "countAuthors",
        tags = ["Authors"],
        responses = [OpenApiResponse("200", [OpenApiContent(Int::class)])]
    )
    private fun countAuthors(ctx: Context) {
        ctx.json(authorController.getCount())
    }

    @OpenApi(
        summary = "Get Author quotes",
        operationId = "getAuthorQuotes",
        tags = ["Authors"],
        responses = [OpenApiResponse("200", [OpenApiContent(Array<QuoteDTO>::class)])]
    )
    private fun getAuthorQuotes(ctx: Context) {
        val requestID = ctx.body<RequestID>()
        ctx.json(authorController.getQuotesFromAuthor(requestID.id))
    }

    @OpenApi(
        summary = "Get Author with most quotes",
        operationId = "getTopAuthor",
        tags = ["Authors"],
        responses = [OpenApiResponse("200", [OpenApiContent(AuthorDTO::class)])]
    )
    private fun getTopAuthor(ctx: Context) {
        ctx.json(authorController.getAuthorWithMostQuotes())
    }

    @OpenApi(
        summary = "Add author to server",
        operationId = "addAuthor",
        tags = ["Authors"],
        requestBody = OpenApiRequestBody([OpenApiContent(AuthorDTO::class)]),
        responses = [OpenApiResponse("200", [OpenApiContent(Response::class)])]
    )
    private fun addAuthor(ctx: Context) {
        val response = try {
            val authorDTO = ctx.body<AuthorDTO>()
            authorController.insert(authorDTO)
            Response(
                code = 200,
                message = "Author successfully added "
            )
        } catch (e: Exception) {
            Response(
                code = 603,
                message = "Error when trying to insert author"
            )
        }

        ctx.json(response)
    }

    @OpenApi(
        summary = "Delete author from server",
        operationId = "deleteAuthor",
        tags = ["Authors"],
        requestBody = OpenApiRequestBody([OpenApiContent(RequestID::class)]),
        responses = [OpenApiResponse("200", [OpenApiContent(Response::class)])]
    )
    private fun deleteAuthor(ctx: Context) {
        val response = try {
            val requestID = ctx.body<RequestID>()
            authorController.remove(requestID.id)
            Response(
                code = 200,
                message = "Author successfully deleted "
            )
        } catch (e: Exception) {
            Response(
                code = 603,
                message = "Error when trying to delete author"
            )
        }
        ctx.json(response)
    }

    @OpenApi(
        summary = "Update author in server",
        operationId = "updateAuthor",
        tags = ["Authors"],
        requestBody = OpenApiRequestBody([OpenApiContent(AuthorDTO::class)]),
        responses = [OpenApiResponse("200", [OpenApiContent(Response::class)])]
    )
    private fun updateAuthor(ctx: Context) {
        val response = try {
            val authorDTO = ctx.body<AuthorDTO>()
            authorController.update(authorDTO)
            Response(
                code = 200,
                message = "Author successfully updated "
            )
        } catch (e: Exception) {
            Response(
                code = 603,
                message = "Error when trying to update author"
            )
        }
        ctx.json(response)
    }
}