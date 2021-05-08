package org.wcode.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*

fun Application.configureModules() {
    install(ContentNegotiation) {
        json()
    }
}
