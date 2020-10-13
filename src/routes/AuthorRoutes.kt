package wcode.software.routes

import io.javalin.Javalin
import wcode.software.database.controllers.AuthorDAO
import wcode.software.dtos.AuthorDTO
import wcode.software.models.RequestID
import wcode.software.models.Response

object AuthorRoutes {

    private val authorController = AuthorDAO()

    fun addRoutes(javalin: Javalin) {
        with(javalin) {
            get("/authors") {
                it.json(authorController.getAll())
            }
            get("/authors/count") {
                it.json(authorController.getCount())
            }
            post("/authors/quotes/") {
                val requestID = it.body<RequestID>()
                it.json(authorController.getQuotesFromAuthor(requestID.id))
            }
            get("/authors/top") {
                it.json(authorController.getAuthorWithMostQuotes())
            }
            post("/authors/add") {
                val response = try {
                    val authorDTO = it.body<AuthorDTO>()
                    authorController.insert(authorDTO)
                    Response(
                        code = 200,
                        message = "Author successfully added "
                    )
                } catch (e: Exception) {
                    Response(
                        code = 503,
                        message = "Error when trying to insert author"
                    )
                }
                it.json(response)
            }
            delete("/authors/delete") {
                val response = try {
                    val requestID = it.body<RequestID>()
                    authorController.remove(requestID.id)
                    Response(
                        code = 200,
                        message = "Author successfully deleted "
                    )
                } catch (e: Exception) {
                    Response(
                        code = 503,
                        message = "Error when trying to delete author"
                    )
                }

                it.json(response)
            }
            put("/authors/update") {
                val response = try {
                    val authorDTO = it.body<AuthorDTO>()
                    authorController.update(authorDTO)
                    Response(
                        code = 200,
                        message = "Author successfully updated "
                    )
                } catch (e: Exception) {
                    Response(
                        code = 503,
                        message = "Error when trying to update author"
                    )
                }

                it.json(response)
            }
        }
    }
}