package br.com.erudio.unittests.mockito.services

import br.com.erudio.exception.RequiredObjectIsNullException
import br.com.erudio.model.Book
import br.com.erudio.repository.BookRepository
import br.com.erudio.services.BookServices
import br.com.erudio.unittests.mocks.MockBook
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*
import java.util.stream.Collectors

//@RunWith(MockitoJUnitRunner::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension::class)
@ExtendWith(SpringExtension::class)
//@MockitoSettings(strictness = Strictness.WARN)
class BookServicesTest {

    private var input: MockBook? = null

    @MockBean
    //var repository: BookRepository? = null
    private lateinit var repository: BookRepository

    @InjectMocks
    private lateinit var service: BookServices
    //var service: BookServices? = null

    @BeforeEach
    fun setupMock() {
        input = MockBook()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun findAll() {
        val list: List<Book> = input!!.mockEntityList()
        `when`(repository!!.findAll()).thenReturn(list)
        val books = service!!.findAll()
        assertNotNull(books)
        assertEquals(14, books.size)
        val bookOne = books[1]
        assertNotNull(bookOne)
        assertNotNull(bookOne.key)
        assertNotNull(bookOne.links)
        assertTrue(bookOne.links.toString().contains("</api/book/v1/1>;rel=\"self\""))
        assertEquals("Some Author1", bookOne.author)
        assertEquals("Some Title1", bookOne.title)
        assertEquals(25.0, bookOne.price)
        //assertNotNull(bookOne.launchDate)

        val bookFour = books[4]
        assertNotNull(bookFour)
        assertNotNull(bookFour.key)
        assertNotNull(bookFour.links)
        assertTrue(bookFour.links.toString().contains("</api/book/v1/4>;rel=\"self\""))
        assertEquals("Some Author4", bookFour.author)
        assertEquals("Some Title4", bookFour.title)
        assertEquals(25.0, bookFour.price)
        //assertNotNull(bookFour.launchDate)
        val bookSeven = books[7]
        assertNotNull(bookSeven)
        assertNotNull(bookSeven.key)
        assertNotNull(bookSeven.links)
        assertTrue(bookSeven.links.toString().contains("</api/book/v1/7>;rel=\"self\""))
        assertEquals("Some Author7", bookSeven.author)
        assertEquals("Some Title7", bookSeven.title)
        assertEquals(25.0, bookSeven.price)
        //assertNotNull(bookSeven.launchDate)
    }

    @Test
    fun findById() {
        val book = input!!.mockEntity(1)
        book.id = 1L
        `when`(repository!!.findById(1L)).thenReturn(Optional.of(book))
        val result = service!!.findById(1L)
        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("</api/book/v1/1>;rel=\"self\""))
        assertEquals("Some Author1", result.author)
        assertEquals("Some Title1", result.title)
        assertEquals(25.0, result.price)
        //assertNotNull(result.launchDate)
    }

    @Test
    fun create() {
        val entity = input!!.mockEntity(1)

        val persisted = entity.copy();
        persisted.id = 1L

        val vo = input!!.mockVO(1)
        vo.key = 1L

        `when`(repository!!.save(entity)).thenReturn(persisted)
        val result = service!!.create(vo)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("</api/book/v1/1>;rel=\"self\""))
        assertEquals("Some Author1", result.author)
        assertEquals("Some Title1", result.title)
        assertEquals(25.0, result.price)
        //assertNotNull(result.launchDate)
    }

    @Test
    fun testCreateWithNullBook() {
        val exception: Exception = assertThrows(
            RequiredObjectIsNullException::class.java
        ) { service!!.create(null) }
        val expectedMessage = "It is not allowed to persist a null object!"
        val actualMessage = exception.message
        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun testUpdate() {
        val entity = input!!.mockEntity(1)
        entity.id = 1L

        val persisted = entity.copy()
        persisted.id = 1L

        val vo = input!!.mockVO(1)
        vo.key = 1L

        `when`(repository!!.findById(1L)).thenReturn(Optional.of(entity))

        `when`(repository!!.save(entity)).thenReturn(persisted)

        val result = service!!.update(vo)
        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("</api/book/v1/1>;rel=\"self\""))
        assertEquals("Some Author1", result.author)
        assertEquals("Some Title1", result.title)
        assertEquals(25.0, result.price)
        //assertNotNull(result.launchDate)
    }

    @Test
    fun testUpdateWithNullBook() {
        val exception: Exception = assertThrows(
            RequiredObjectIsNullException::class.java
        ) { service!!.update(null) }
        val expectedMessage = "It is not allowed to persist a null object!"
        val actualMessage = exception.message
        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun testDelete() {
        val book = input!!.mockEntity(1)
        book.id = 1L
        `when`(repository!!.findById(1L)).thenReturn(Optional.of(book))
        service!!.delete(1L)
    }
}