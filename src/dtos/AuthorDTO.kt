package wcode.software.dtos

import wcode.software.base.BaseDTO
import wcode.software.database.models.Author

data class AuthorDTO(
    val id: String? = null,
    val name: String,
    val picUrl: String? = null
) : BaseDTO {
    constructor(author: Author) : this(
        id = author.id.toString(),
        name = author.name,
        picUrl = author.picUrl
    )
}