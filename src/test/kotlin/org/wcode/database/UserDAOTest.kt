package org.wcode.database

import org.junit.Before
import org.junit.Test
import org.wcode.core.Cryptography
import org.wcode.core.Cryptography.decrypt
import org.wcode.core.H2Connector
import org.wcode.database.sql.dao.UserDAO
import org.wcode.dto.UserDTO
import java.util.*
import kotlin.test.assertEquals

class UserDAOTest {

    private val database = H2Connector().init()

    private val userDAO = UserDAO(database)

    @Before
    fun initTest(){
        Cryptography.initCipher()
    }

    @Test
    fun `Get User by ID`() {
        val user = UserDTO(email = "user@test.com", password = "test")
        userDAO.insert(user)

        userDAO.getById(user.id).onSuccess {
            assertEquals(user.id, it.id)
        }.onFailure {
            assert(false)
        }

        userDAO.delete(user.id)
    }

    @Test
    fun `Update user`() {
        val user = UserDTO(email = "user@test.com", password = "test")
        userDAO.insert(user)

        userDAO.getById(user.id).onSuccess {
            assertEquals(user.id, it.id)
        }.onFailure {
            assert(false)
        }

        val newUser = user.copy(id = user.id, password = "New_password")
        userDAO.update(newUser).onSuccess {
            assertEquals(newUser.password,it.password.decrypt())
        }.onFailure {
            assert(false)
        }

        userDAO.delete(user.id)
    }

    @Test
    fun `User exist for non existent user`(){
        assert(!userDAO.userExist(UUID.randomUUID().toString()))
    }

    @Test
    fun `User exist`(){
        val user = UserDTO(email = "user@test.com", password = "test")
        userDAO.insert(user)

        assert(userDAO.userExist(user.id))

        userDAO.delete(user.id)
    }
}
