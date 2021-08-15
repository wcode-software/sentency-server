package org.wcode.author.service.core

import io.micronaut.context.annotation.Value
import io.sentry.Sentry
import javax.inject.Singleton

@Singleton
class SentryManager(
    @Value("\${sentry.dns}") private val dns: String,
    @Value("\${sentry.environment}") private val environment: String
) {

    init {
        configureSentry()
    }

    private val isDebug: Boolean = environment == "debug"

    private fun configureSentry() {
        Sentry.init { options ->
            options.dsn = dns
            options.tracesSampleRate = 0.1
            options.setDebug(isDebug)
            options.environment = environment
            options.release = "v1.0.0"
        }
    }
}
