package wcode.software.routes

import io.javalin.Javalin
import wcode.software.database.controllers.QuoteDAO
import wcode.software.dtos.QuoteDTO
import wcode.software.models.RequestID
import wcode.software.models.Response

object QuoteRoutes {

    private val quoteController = QuoteDAO()

    fun addRoutes(javalin: Javalin) {
        with(javalin) {
            get("/quotes") {
                it.json(quoteController.getAll())
            }
            get("/quotes/count") {
                it.json(quoteController.getCount())
            }
            get("/quotes/random") {
                it.json(quoteController.getRandom())
            }
            get("/quotes/month/count"){
                it.json(quoteController.getMonthCount())
            }
            post("/quotes/add") {
                val quoteDTO = it.body<QuoteDTO>()
                val response = try {
                    quoteController.insert(quoteDTO)
                    Response(
                        code = 200,
                        message = "Quote successfully added "
                    )
                } catch (e: Exception) {
                    Response(
                        code = 503,
                        message = "Error when trying to insert quote"
                    )
                }

                it.json(response)
            }
            delete("/quotes/delete") {
                val requestID = it.body<RequestID>()
                val response = try {
                    quoteController.remove(requestID.id)
                    Response(
                        code = 200,
                        message = "Quote successfully deleted "
                    )
                } catch (e: Exception) {
                    Response(
                        code = 503,
                        message = "Error when trying to delete quote"
                    )
                }

                it.json(response)
            }
            put("/quotes/update") {
                val quoteDTO = it.body<QuoteDTO>()
                val response = try {
                    quoteController.update(quoteDTO)
                    Response(
                        code = 200,
                        message = "Quote successfully updated "
                    )
                } catch (e: Exception) {
                    Response(
                        code = 503,
                        message = "Error when trying to update quote"
                    )
                }

                it.json(response)
            }
        }
    }
}
