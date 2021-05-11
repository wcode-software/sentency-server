package org.wcode.database.schema

import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.wcode.database.core.BaseSchema
import org.wcode.database.tables.AuthorTable
import org.wcode.database.tables.QuoteTable
import org.wcode.dto.AuthorDTO
import java.util.*

class AuthorSchema(id: EntityID<UUID>) : BaseSchema<AuthorDTO>(id) {

    companion object : UUIDEntityClass<AuthorSchema>(AuthorTable)

    var name by AuthorTable.name
    var picUrl by AuthorTable.picUrl

    val quotes by QuoteSchema referrersOn QuoteTable.author

    override fun toDTO(): AuthorDTO = AuthorDTO(
        id = this.id.toString(),
        name = this.name,
        picUrl = this.picUrl
    )
}
