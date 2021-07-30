package org.wcode.author.service.services

import org.wcode.author.service.domain.AuthorRepository
import org.wcode.author.service.domain.entities.AuthorDB
import org.wcode.author.service.models.Author
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorService {

    @Inject
    lateinit var authorRepository: AuthorRepository

    fun listAllAuthors(): List<Author> {
        return authorRepository.findAll().map { it.toDTO() }
    }

    fun getById(id: String): Author? {
        val authorDB = authorRepository.findById(id).orElse(null)
        return authorDB?.toDTO()
    }

    fun deleteById(id: String): Boolean {
        return try {
            authorRepository.findById(id).orElse(null)?.let {
                authorRepository.deleteById(id)
                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    fun insertAuthor(author: Author): Author {
        val authorDB = AuthorDB(name = author.name, picUrl = author.picUrl)
        author.id?.let {
            authorDB.id = it
        }
        authorRepository.save(authorDB)
        return authorDB.toDTO()
    }

    fun updateAuthor(author: Author): Author? {
        return author.id?.let {
            authorRepository.findById(it).orElse(null)?.let { authorDB ->
                authorDB.updateEntry(author)
                authorRepository.update(authorDB)
                authorDB.toDTO()
            }
        }
    }
}
