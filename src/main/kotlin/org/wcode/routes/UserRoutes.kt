package org.wcode.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.wcode.core.EnvironmentConfig
import org.wcode.database.dao.UserDAO
import org.wcode.dto.LoginUserDTO
import org.wcode.dto.PaginatedDTO
import org.wcode.dto.QuoteDTO
import org.wcode.dto.UserDTO
import org.wcode.interfaces.BaseRoute

class UserRoutes : BaseRoute, KoinComponent {

    private val userDAO: UserDAO by inject()

    override fun setupRouting(routing: Routing) {
        routing {
            route("/user") {
                header("apiKey", EnvironmentConfig.apiKey) {
                    authenticate {
                        createUser()
                        getPaginated()
                        getAll()
                        deleteUser()
                    }
                    login()
                }
            }
        }
    }

    private fun Route.createUser() {
        post {
            val user = call.receive<UserDTO>()
            userDAO.insert(user).onSuccess {
                it.hidePassword()
                call.respond(it)
            }.onFailure {
                call.respondText("Error when adding user", status = HttpStatusCode.NotModified)
            }
        }
    }

    private fun Route.login() {
        post("/login") {
            val user = call.receive<LoginUserDTO>()
            userDAO.login(user.email, user.password).onSuccess {
                call.respond(it)
            }.onFailure {
                call.respondText("Failure when trying to login", status = HttpStatusCode.Unauthorized)
            }
        }
    }

    private fun Route.getAll() {
        get("/all") {
            userDAO.list(all = true).onSuccess {
                it.forEach { it.hidePassword() }
                call.respond(it)
            }.onFailure {
                call.respondText("Failure when retrieving users", status = HttpStatusCode.InternalServerError)
            }
        }
    }

    private fun Route.getPaginated() {
        get {
            val query = call.request.queryParameters
            val page = Integer.valueOf(query["page"] ?: "1")
            val size = Integer.valueOf(query["size"] ?: "10")
            val count = userDAO.count()
            userDAO.list(page, size).onSuccess {
                it.forEach { it.hidePassword() }
                val response = PaginatedDTO(it, page, size, call.request.path(), count)
                call.respond(response)
            }.onFailure {
                call.respondText("Failure when retrieving users", status = HttpStatusCode.InternalServerError)
            }
        }
    }

    private fun Route.deleteUser() {
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            userDAO.delete(id).onSuccess {
                call.respondText("User removed correctly", status = HttpStatusCode.Accepted)
            }.onFailure {
                call.respondText("User not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
