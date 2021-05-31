package org.wcode.plugins

import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import org.eclipse.jetty.util.log.Log
import org.wcode.settings.EnvironmentConfig
import org.wcode.settings.EnvironmentConfig.initializeConfig

fun Application.configureSettings() {
    val applicationConfig = HoconApplicationConfig(ConfigFactory.load())
    initializeConfig(applicationConfig)
}
