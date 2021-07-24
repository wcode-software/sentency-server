package org.wcode.author.service.controllers

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import org.wcode.author.service.models.Author

@Controller("/author")
class AuthorController {

    private val authors = mutableListOf<Author>()

    @Get("/all")
    fun listAllAuthors(): List<Author> {
        return authors
    }

    @Get("/{id}")
    fun getById(id: String): Author? {
        return authors.find { it.id == id }
    }

    @Delete("/{id}")
    fun deleteById(id: String): Boolean {
        return authors.removeIf { it.id == id }
    }

    @Post("/")
    fun insertAuthor(author: Author): Author {
        authors.add(author)
        return author
    }
}
