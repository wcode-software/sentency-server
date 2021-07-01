package org.wcode.database.sql

object DatabaseConstants {
    object Quote {
        const val author = "quote_author"
        const val message = "quote_message"
        const val timestamp = "quote_timestamp"
        const val type = "quote_type"
    }

    object QuoteLocalization {
        const val quoteId = "localization_quote_id"
        const val code = "localization_quote_code"
        const val message = "localization_quote_message"
    }

    object Author {
        const val id = "author_id"
        const val name = "author_name"
        const val picUrl = "author_pic_url"
    }

    object User {
        const val email = "user_email"
        const val password = "user_password"
    }
}
