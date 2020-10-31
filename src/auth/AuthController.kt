package wcode.software.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.javalin.core.util.Header
import io.javalin.http.Context
import wcode.software.config.Environment
import wcode.software.data.dtos.UserDTO
import java.lang.NullPointerException

object AuthController {

    private val algorithm = Algorithm.HMAC512(Environment.jwtSecretkey)
    val verifier: JWTVerifier = JWT.require(algorithm).build()

    fun encryptPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun checkPassword(password: String, hashPassword: String): Boolean {
        val result: BCrypt.Result = BCrypt.verifyer().verify(password.toCharArray(), hashPassword)
        return result.verified
    }

    fun generateToken(user: UserDTO): String {
        return JWT
            .create()
            .withClaim("email", user.email)
            .withClaim("auth", user.password)
            .sign(algorithm)
    }

    fun headerDecoderHandler(ctx: Context): Context {
        if (ctx.method() == "OPTIONS") {
            ctx.header(Header.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
        } else {
            val token = ctx.header("Authorization") ?: throw NullPointerException("Token not found in header")
            ctx.attribute("TOKEN", token)
        }
        return ctx
    }
}