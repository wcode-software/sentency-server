package org.wcode.database

import org.junit.Test
import org.wcode.database.connections.H2Connection
import org.wcode.database.dao.AuthorDAO
import org.wcode.dto.AuthorDTO
import kotlin.test.assertEquals

class AuthorDAOTest {

    private val database = H2Connection().init()
    private val authorDao = AuthorDAO(database)
    private val author = AuthorDTO(id = "Test", name = "Test", picUrl = "TestPic")

    @Test
    fun `Test create Author`() {
        authorDao.insert(author).onSuccess {
            assertEquals(author.id, it.id)
            assertEquals(author.name, it.name)
            assertEquals(author.picUrl, it.picUrl)
            authorDao.delete(author.id)
        }
    }

    @Test
    fun `Test update Author`() {
        authorDao.insert(author).onSuccess {
            assertEquals(author.id, it.id)
            assertEquals(author.name, it.name)
            assertEquals(author.picUrl, it.picUrl)
        }

        val newAuthor = AuthorDTO(id = "Test", name = "Test", picUrl = "TestPic Updated")
        authorDao.update(newAuthor).onSuccess {
            assertEquals(newAuthor.id, it.id)
            assertEquals(newAuthor.name, it.name)
            assertEquals(newAuthor.picUrl, it.picUrl)
            authorDao.delete(author.id)
        }
    }

    @Test
    fun `Test update Author failing`() {
        authorDao.update(author).onFailure {
            assert(true)
        }
    }

    @Test
    fun `Test count endpoint`() {
        val author2 = AuthorDTO(id = "Test2", name = "Test", picUrl = "TestPic")
        val result = authorDao.insert(author)
        if (result.isSuccess) {
            val count1 = authorDao.count()
            assertEquals(1, count1)
        }
        authorDao.insert(author2).onSuccess {
            assertEquals(2, authorDao.count())
        }
        authorDao.delete(author.id)
        authorDao.delete(author2.id).onSuccess {
            assertEquals(0, authorDao.count())
        }
    }

    @Test
    fun `Test list all authors`() {
        val author2 = AuthorDTO(id = "Test2", name = "Test", picUrl = "TestPic")
        authorDao.insert(author).onSuccess {
            assertEquals(1, authorDao.count())
        }
        authorDao.insert(author2).onSuccess {
            authorDao.list(all = true).onSuccess { list ->
                assert(list.find { it.id == author.id } != null)
                assert(list.find { it.id == author2.id } != null)
            }
        }
        authorDao.delete(author.id)
        authorDao.delete(author2.id)
    }

    @Test
    fun `Test list authors paginated`() {
        val author2 = AuthorDTO(id = "Test2", name = "Test", picUrl = "TestPic")
        authorDao.insert(author).onSuccess {
            assertEquals(1, authorDao.count())
        }
        authorDao.insert(author2).onSuccess {
            authorDao.list(page = 1, size = 1).onSuccess { list ->
                assertEquals(1, list.size)
            }
        }
        authorDao.delete(author.id)
        authorDao.delete(author2.id)
    }

    @Test
    fun `Test get Author by ID`() {
        authorDao.insert(author).onSuccess {
            assertEquals(1, authorDao.count())
        }

        authorDao.getById(author.id).onSuccess {
            assertEquals(author.id, it.id)
            authorDao.delete(author.id)
        }
    }

    @Test
    fun `Test get Author by ID failure`() {
        authorDao.getById(author.id).onFailure {
            assert(true)
        }
    }

    @Test
    fun `Test delete Author that exist`() {
        authorDao.insert(author).onSuccess {
            assertEquals(1, authorDao.count())
        }

        authorDao.delete(author.id).onSuccess {
            assert(it)
        }
    }

    @Test
    fun `Test delete Author that doesn't exist`() {
        authorDao.delete(author.id).onFailure {
            assert(true)
        }
    }
}
