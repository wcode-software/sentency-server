package org.wcode.core

import io.ktor.config.*
import org.eclipse.jetty.util.log.Log

object EnvironmentConfig {

    var flavor: String = "development"
        private set
    var databaseName: String = "sentency_db"
        private set

    var databaseUsername: String = ""
        private set

    var databasePassword: String = ""
        private set

    var apiKey: String = "APIKEY"
        private set

    var secret: String = "JWT_SECRET"
        private set

    fun initializeConfig(applicationConfig: ApplicationConfig) {
        flavor = applicationConfig.propertyOrNull("ktor.flavor")?.getString() ?: "development"
        apiKey = applicationConfig.propertyOrNull("ktor.apiKey")?.getString() ?: "APIKEY"
        Log.getLog().info("Settings initialized on flavor $flavor")

        initializeDatabase(applicationConfig)
        initializeJWT(applicationConfig)
    }

    private fun initializeDatabase(applicationConfig: ApplicationConfig) {
        databaseName = applicationConfig.propertyOrNull("database.name")?.getString() ?: "sentency_db"
        databaseUsername = applicationConfig.propertyOrNull("database.username")?.getString() ?: ""
        databasePassword = applicationConfig.propertyOrNull("database.password")?.getString() ?: ""
    }

    private fun initializeJWT(applicationConfig: ApplicationConfig) {
        secret = applicationConfig.propertyOrNull("jwt.secret")?.getString() ?: "1234567890abcdefghijklmn"
    }
}
