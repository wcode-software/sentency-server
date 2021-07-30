package org.wcode.author.service

import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("org.wcode.author.service")
        .start()
}


