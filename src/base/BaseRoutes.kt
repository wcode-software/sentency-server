package wcode.software.base

import io.javalin.Javalin

abstract class BaseRoutes {

    abstract val basePath: String

    abstract fun addRoutes(javalin: Javalin)
}
