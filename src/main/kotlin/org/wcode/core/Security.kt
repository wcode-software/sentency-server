package org.wcode.core

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.jwt.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.wcode.database.sql.dao.UserDAO
import org.wcode.dto.UserDTO
import java.util.*

object Security : KoinComponent {

    private val userDao: UserDAO by inject()

    private val issuer = EnvironmentConfig.issuer
    private const val validityInMs = 36_000_00 * 10 // 10 hours
    private val algorithm = Algorithm.HMAC256(EnvironmentConfig.secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)

    fun validateCredential(jwtCredential: JWTCredential): JWTPrincipal? {
        val payload = jwtCredential.payload
        return when {
            userDao.userExist(payload.getClaim("id").asString()) -> JWTPrincipal(payload)
            else -> null
        }
    }

    fun skipAuth(): Boolean{
        return userDao.count() == 0
    }

    fun UserDTO.makeToken(): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", this.id)
        .withClaim("email", this.email)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

}
