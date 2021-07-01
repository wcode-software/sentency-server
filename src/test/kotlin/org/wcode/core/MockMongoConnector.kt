package org.wcode.core

import com.mongodb.client.MongoDatabase
import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import org.litote.kmongo.KMongo
import org.wcode.database.nosql.core.BaseNoSQLConnector


class MockMongoConnector : BaseNoSQLConnector() {

    override fun createDB(): MongoDatabase {
        val server = MongoServer(MemoryBackend())
        val address = server.bind()

        val client = KMongo.createClient("mongodb://${address.hostName}:${address.port}")
        return client.getDatabase("sentency")
    }
}
