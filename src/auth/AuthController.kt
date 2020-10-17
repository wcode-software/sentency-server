package wcode.software.auth

import at.favre.lib.crypto.bcrypt.BCrypt

object AuthController {

    fun encryptPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun checkPassword(password: String, hashPassword: String): Boolean {
        val result: BCrypt.Result = BCrypt.verifyer().verify(password.toCharArray(), hashPassword)
        return result.verified
    }
}