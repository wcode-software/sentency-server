package org.wcode.author.service.domain.entities

import org.wcode.author.service.models.Author
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class AuthorDB(
    @Id var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var picUrl: String? = null
) {
    fun toDTO(): Author = Author(id, name, picUrl)

    fun updateEntry(author: Author) {
        this.name = author.name
        this.picUrl = author.picUrl
    }
}
