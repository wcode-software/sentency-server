package org.wcode.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import org.wcode.core.Security
import org.wcode.core.Security.validateCredential

fun Application.configureAuth() {
    install(Authentication) {
        jwt {
            verifier(Security.verifier)
            validate {
                validateCredential(it)
            }
        }
    }
}
