package org.wcode.plugins

import io.ktor.application.*
import org.wcode.core.Cryptography.initCipher
import org.wcode.core.EnvironmentConfig.initializeConfig

fun Application.configureSettings() {
    initializeConfig(environment.config)
    initCipher()
}
