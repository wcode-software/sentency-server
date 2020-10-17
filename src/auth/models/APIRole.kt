package wcode.software.auth.models

import io.javalin.core.security.Role

enum class APIRole: Role {
    ANYONE, USER, ADMIN
}