package org.wcode.plugins

import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import org.wcode.core.Cryptography.initCipher
import org.wcode.core.EnvironmentConfig.initializeConfig

fun Application.configureSettings() {
    val applicationConfig = HoconApplicationConfig(ConfigFactory.load())
    initializeConfig(applicationConfig)
    initCipher()
}
