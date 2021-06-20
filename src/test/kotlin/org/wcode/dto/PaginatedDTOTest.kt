package org.wcode.dto

import org.junit.Test
import kotlin.test.assertEquals

class PaginatedDTOTest {

    @Test
    fun `Paginated DTO next prev page null`() {
        val mList = listOf("T1", "T2", "T3")
        val paginatedDTO = PaginatedDTO<List<String>>(
            data = mList,
            page = 1,
            size = mList.size,
            path = "/test",
            total = mList.size
        )
        assertEquals(null, paginatedDTO.prev)
        assertEquals(null, paginatedDTO.next)
    }

    @Test
    fun `Paginated DTO prev null, next page`() {
        val mList = listOf("T1", "T2", "T3")
        val paginatedDTO = PaginatedDTO<List<String>>(
            data = mList,
            page = 1,
            size = mList.size,
            path = "/test",
            total = 10
        )
        assertEquals(null, paginatedDTO.prev)
        assertEquals("/test?page=2&size=3", paginatedDTO.next)
    }

    @Test
    fun `Paginated DTO prev page, next null`() {
        val mList = listOf("T1", "T2", "T3")
        val paginatedDTO = PaginatedDTO<List<String>>(
            data = mList,
            page = 2,
            size = mList.size,
            path = "/test",
            total = 6
        )
        assertEquals("/test?page=1&size=3", paginatedDTO.prev)
        assertEquals(null, paginatedDTO.next)
    }

    @Test
    fun `Paginated DTO prev page, next page`() {
        val mList = listOf("T1", "T2", "T3")
        val paginatedDTO = PaginatedDTO<List<String>>(
            data = mList,
            page = 2,
            size = mList.size,
            path = "/test",
            total = 10
        )
        assertEquals("/test?page=1&size=3", paginatedDTO.prev)
        assertEquals("/test?page=3&size=3", paginatedDTO.next)
    }
}
