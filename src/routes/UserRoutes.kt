package wcode.software.routes

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import wcode.software.auth.AuthController
import wcode.software.base.BaseRoutes
import wcode.software.data.database.daos.UserDAO
import wcode.software.data.dtos.UserDTO
import wcode.software.models.Response

class UserRoutes : BaseRoutes() {

    private val userController = UserDAO()
    override val basePath: String = "/users"

    override fun addRoutes(javalin: Javalin) {
        javalin.routes {
            path(basePath) {
                post("/create", ::createUser)
                post("/authenticate", ::authenticateUser)
                get("/validate", ::validateToken)
            }
        }
    }

    @OpenApi(
        summary = "Create user on server",
        operationId = "createUser",
        tags = ["User"],
        requestBody = OpenApiRequestBody([OpenApiContent(UserDTO::class)]),
        responses = [OpenApiResponse("200", [OpenApiContent(Response::class)])]
    )
    private fun createUser(ctx: Context) {
        val response = try {
            val userDTO = ctx.body<UserDTO>()
            val bashPassword = AuthController.encryptPassword(userDTO.password)
            val newUser = UserDTO(email = userDTO.email, password = bashPassword, role = userDTO.role)
            userController.insert(newUser)
            Response(
                code = 200,
                message = AuthController.generateToken(newUser)
            )
        } catch (e: Exception) {
            Response(
                code = 603,
                message = "Error when trying to create user"
            )
        }

        ctx.json(response)
    }

    @OpenApi(
        summary = "Authenticate user",
        operationId = "authenticateUser",
        tags = ["User"],
        requestBody = OpenApiRequestBody([OpenApiContent(UserDTO::class)]),
        responses = [OpenApiResponse("200", [OpenApiContent(Response::class)])]
    )
    private fun authenticateUser(ctx: Context) {
        val response = try {
            val userDTO = ctx.body<UserDTO>()
            val dbUser = userController.getUser(userDTO.email) ?: throw NullPointerException()
            val valid = AuthController.checkPassword(userDTO.password, dbUser.password)
            if (valid) {
                Response(
                    code = 200,
                    message = AuthController.generateToken(dbUser)
                )
            } else {
                Response(
                    code = 403,
                    message = "Wrong credentials"
                )
            }
        } catch (e: Exception) {
            Response(
                code = 604,
                message = "User not found in the database"
            )
        }

        ctx.json(response)
    }

    private fun validateToken(ctx: Context) {
        val response = try {
            val header =
                ctx.header("Authorization") ?: throw NullPointerException("No Authorization found in header")

            AuthController.verifier.verify(header)
            Response(
                code = 200,
                message = "Token valid authorized"
            )
        } catch (e: Exception) {
            Response(
                code = 403,
                message = "Problem with authorization: ${e.message}"
            )
        }

        ctx.json(response)
    }
}
