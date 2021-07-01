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

    var secret: String = "1234567890abcdefghijklmn"
        private set

    var issuer: String = "sentency.io"
        private set

    var mongoHost: String = "localhost"
        private set

    fun initializeConfig(applicationConfig: HoconApplicationConfig) {
        flavor = applicationConfig.propertyOrNull("ktor.flavor")?.getString() ?: "development"
        apiKey = applicationConfig.propertyOrNull("ktor.apiKey")?.getString() ?: "APIKEY"
        Log.getLog().info("Settings initialized on flavor $flavor")

        initializeDatabase(applicationConfig)
        initializeJWT(applicationConfig)
        initializeMongo(applicationConfig)
    }

    private fun initializeDatabase(applicationConfig: HoconApplicationConfig) {
        databaseName = applicationConfig.propertyOrNull("database.name")?.getString() ?: "sentency_db"
        databaseUsername = applicationConfig.propertyOrNull("database.username")?.getString() ?: ""
        databasePassword = applicationConfig.propertyOrNull("database.password")?.getString() ?: ""
    }

    private fun initializeJWT(applicationConfig: HoconApplicationConfig) {
        secret = applicationConfig.propertyOrNull("jwt.secret")?.getString() ?: "1234567890abcdefghijklmn"
        issuer = applicationConfig.propertyOrNull("jwt.issuer")?.getString() ?: "sentency.io"
    }

    private fun initializeMongo(applicationConfig: HoconApplicationConfig) {
        mongoHost = applicationConfig.propertyOrNull("mongo.host")?.getString() ?: "localhost"
    }
}
