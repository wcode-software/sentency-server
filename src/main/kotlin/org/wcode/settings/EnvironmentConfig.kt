package org.wcode.settings

import io.ktor.config.*
import org.eclipse.jetty.util.log.Log

object EnvironmentConfig {

    var flavor: String = "development"
    var databaseName: String = "sentency_db"
    var databaseUsername: String = ""
    var databasePassword: String = ""
    var apiKey: String = "APIKEY"

    fun initializeConfig(applicationConfig: HoconApplicationConfig) {
        val ktorValues = applicationConfig.config("ktor")
        flavor = ktorValues.propertyOrNull("flavor")?.getString() ?: "development"
        apiKey = ktorValues.propertyOrNull("apiKey")?.getString() ?: "APIKEY"

        Log.getLog().info("Settings initialized on flavor $flavor")

        val databaseConfig = ktorValues.config("database")

        databaseName = databaseConfig.propertyOrNull("name")?.getString() ?: "sentency_db"
        databaseUsername = databaseConfig.propertyOrNull("username")?.getString() ?: ""
        databasePassword = databaseConfig.propertyOrNull("password")?.getString() ?: ""
    }
}
