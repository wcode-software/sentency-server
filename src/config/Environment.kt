package wcode.software.config

import io.github.cdimascio.dotenv.Dotenv

object Environment {

    var environment: String = ""
        get() = field
        private set(value) {
            field = value
        }

    var db_user: String = ""
        get() = field
        private set(value) {
            field = value
        }


    var db_password: String = ""
        get() = field
        private set(value) {
            field = value
        }

    var db_name: String = ""
        get() = field
        private set(value) {
            field = value
        }

    fun startEnvironment(dotenv: Dotenv) {
        environment = dotenv["ENVIRONMENT"]
        db_user = dotenv["DB_USER"]
        db_name = dotenv["DB_NAME"]
        db_password = dotenv["DB_PASSWORD"]
    }
}