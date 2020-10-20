package wcode.software.config

import io.github.cdimascio.dotenv.Dotenv

object Environment {

    var environment: String = ""
        get() = field
        private set(value) {
            field = value
        }

    var dbUser: String = ""
        get() = field
        private set(value) {
            field = value
        }


    var dbPassword: String = ""
        get() = field
        private set(value) {
            field = value
        }

    var dbName: String = ""
        get() = field
        private set(value) {
            field = value
        }

    var jwtSecretkey: String = ""
        get() = field
        private set(value) {
            field = value
        }

    fun startEnvironment(dotenv: Dotenv) {
        environment = dotenv["ENVIRONMENT"]
        dbUser = dotenv["DB_USER"]
        dbName = dotenv["DB_NAME"]
        dbPassword = dotenv["DB_PASSWORD"]
        jwtSecretkey = dotenv["JWT_SECRET_KEY"]
    }
}