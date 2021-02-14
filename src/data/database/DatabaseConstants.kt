package wcode.software.data.database

object DatabaseConstants {

    object Quote {
        const val author = "quote_author"
        const val quote = "quote_message"
        const val timestamp = "quote_timestamp"
        const val type = "quote_type"
    }

    object Author {
        const val id = "id"
        const val name = "author_name"
        const val picUrl = "author_pic_url"
    }

    object User {
        const val email = "user_email"
        const val password = "user_password"
        const val role = "user_role"
        const val picUrl = "user_picture_url"
        const val birthday = "user_birthday"
    }
}
