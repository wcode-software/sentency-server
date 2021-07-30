package org.wcode.author.service.controllers

import io.micronaut.http.annotation.*
import org.wcode.author.service.models.Author
import org.wcode.author.service.models.GenericResponse
import org.wcode.author.service.services.AuthorService
import javax.inject.Inject

@Controller("/author")
class AuthorController {

    @Inject
    lateinit var authorService: AuthorService

    @Get("/all")
    fun listAllAuthors(): List<Author> {
        return authorService.listAllAuthors()
    }

    @Get("/{id}")
    fun getById(id: String): Author? {
        return authorService.getById(id)
    }

    @Delete("/{id}")
    fun deleteById(id: String): GenericResponse {
        return if (authorService.deleteById(id)) {
            GenericResponse(true)
        } else {
            GenericResponse(false, "Unable to delete author")
        }
    }

    @Post("/")
    fun insertAuthor(author: Author): Author {
        return authorService.insertAuthor(author)
    }

    @Put("/")
    fun updateAuthor(author: Author): Author? {
        return authorService.updateAuthor(author)
    }
}
