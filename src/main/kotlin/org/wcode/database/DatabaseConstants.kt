package org.wcode.database

object DatabaseConstants {
    object Quote {
        const val author = "quote_author"
        const val message = "quote_message"
        const val timestamp = "quote_timestamp"
        const val type = "quote_type"
    }

    object Author {
        const val id = "author_id"
        const val name = "author_name"
        const val picUrl = "author_pic_url"
    }
}
