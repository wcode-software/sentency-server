package org.wcode.database.nosql.core

import com.mongodb.client.MongoDatabase


abstract class BaseNoSQLConnector {

    fun init(): MongoDatabase {
        return createDB()
    }

    abstract fun createDB(): MongoDatabase
}
