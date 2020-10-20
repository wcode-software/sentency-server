package wcode.software.database

object DatabaseConstants {

    object Quote {
        const val author = "quote_author"
        const val quote = "quote_message"
        const val timestamp = "quote_timestamp"
    }

    object Author {
        const val id = "id"
        const val name = "author_name"
        const val picUrl = "author_pic_url"
    }

    object User{
        const val email = "email"
        const val password = "password"
        const val role = "role"
    }
}