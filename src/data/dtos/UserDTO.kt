package wcode.software.data.dtos

import wcode.software.auth.models.APIRole
import wcode.software.base.BaseDTO
import wcode.software.data.database.schemas.UserSchema

data class UserDTO(
    val email: String,
    val password: String,
    val role: APIRole = APIRole.ANYONE
) : BaseDTO {
    constructor(_userSchema: UserSchema) : this(
        email = _userSchema.email,
        password = _userSchema.password,
        role = APIRole.values()[_userSchema.role]
    )
}