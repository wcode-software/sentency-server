package wcode.software.dtos

import wcode.software.base.BaseDTO
import wcode.software.database.models.AuthorSchema

data class AuthorDTO(
    val id: String? = null,
    val name: String,
    val picUrl: String? = null
) : BaseDTO {
    constructor(authorSchema: AuthorSchema) : this(
        id = authorSchema.id.toString(),
        name = authorSchema.name,
        picUrl = authorSchema.picUrl
    )
}