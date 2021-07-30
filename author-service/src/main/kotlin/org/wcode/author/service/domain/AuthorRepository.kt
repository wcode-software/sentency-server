package org.wcode.author.service.domain

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.PageableRepository
import org.wcode.author.service.domain.entities.AuthorDB

@Repository
abstract class AuthorRepository : PageableRepository<AuthorDB, String> {
}
