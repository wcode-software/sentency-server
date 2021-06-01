package org.wcode.interfaces

import io.ktor.routing.*

interface BaseRoute {
    fun setupRouting(routing: Routing)

    private fun getBaseUrl() : String{
        return "test"
    }
}
