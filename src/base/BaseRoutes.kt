package wcode.software.base

import io.javalin.Javalin

interface BaseRoutes {

    fun addRoutes(javalin: Javalin)
}