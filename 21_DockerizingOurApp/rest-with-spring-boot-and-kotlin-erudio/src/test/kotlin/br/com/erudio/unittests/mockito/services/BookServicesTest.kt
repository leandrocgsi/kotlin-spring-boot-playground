package br.com.erudio.unittests.mockito.services

import br.com.erudio.data.vo.v1.BookVO
import br.com.erudio.exception.RequiredObjectIsNullException
import br.com.erudio.model.Book
import br.com.erudio.repository.BookRepository
import br.com.erudio.services.BookServices
import br.com.erudio.unittests.mapper.mocks.MockBook
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.lenient
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.springframework.data.domain.*
import java.util.*
import java.util.stream.Collectors


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookServicesTest {

    // https://stackoverflow.com/questions/52139619/simulation-of-service-using-mockito-2-leads-to-stubbing-error

    private var input: MockBook? = null

    @InjectMocks
    private val service: BookServices? = null

    @Mock
    var repository: BookRepository? = null

    @BeforeEach
    fun setupMock() {
        input = MockBook()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testFindAll() {
        val list: List<Book> = input!!.mockEntityList()
        val page: Page<Book> = PageImpl(list)

        val pageable: Pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"))

        `when`(repository!!.findAll(pageable)).thenReturn(page)
        val searchPage = service!!.findAll(pageable)!!.content

        val books: List<BookVO> = searchPage.stream().collect(Collectors.toList<BookVO>()) as List<BookVO>

        assertNotNull(books)
        assertEquals(14, books.size)
        val bookOne = books[1]
        assertNotNull(bookOne)
        assertNotNull(bookOne.key)
        assertNotNull(bookOne.links)
        assertTrue(bookOne.toString().contains("""links: [</api/book/v1/1>;rel="self"]"""))
        assertEquals("Some Author1", bookOne.author)
        assertEquals("Some Title1", bookOne.title)
        assertEquals(25.0, bookOne.price)
        assertNotNull(bookOne.launchDate)

        val bookFour = books[4]
        assertNotNull(bookFour)
        assertNotNull(bookFour.key)
        assertNotNull(bookFour.links)
        assertTrue(bookFour.toString().contains("""links: [</api/book/v1/4>;rel="self"]"""))
        assertEquals("Some Author4", bookFour.author)
        assertEquals("Some Title4", bookFour.title)
        assertEquals(25.0, bookFour.price)
        assertNotNull(bookFour.launchDate)

        val bookSeven = books[7]
        assertNotNull(bookSeven)
        assertNotNull(bookSeven.key)
        assertNotNull(bookSeven.links)
        assertTrue(bookSeven.toString().contains("""links: [</api/book/v1/7>;rel="self"]"""))
        assertEquals("Some Author7", bookSeven.author)
        assertEquals("Some Title7", bookSeven.title)
        assertEquals(25.0, bookSeven.price)
        assertNotNull(bookSeven.launchDate)
    }

    @Test
    fun testFindById() {
        val book = input!!.mockEntity(1)
        book.id = 1L
        `when`(repository!!.findById(1L)).thenReturn(Optional.of(book))
        val result = service!!.findById(1L)
        assertNotNull(result)
        assertNotNull(result!!.key)
        assertNotNull(result.links)
        assertTrue(result.toString().contains("""links: [</api/book/v1/1>;rel="self"]"""))
        assertEquals("Some Author1", result.author)
        assertEquals("Some Title1", result.title)
        assertEquals(25.0, result.price)
        assertNotNull(result.launchDate)
    }

    @Test
    @RepeatedTest(15)
    fun testCreate() {
        val entity = input!!.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1L

        val vo = input!!.mockVO(1)
        vo.key = 1L

        lenient().`when`(repository!!.save(entity)).thenReturn(persisted)
        val result = service!!.create(vo)

        assertNotNull(result)
        assertNotNull(result!!.key)
        assertNotNull(result.links)
        assertTrue(result.toString().contains("""links: [</api/book/v1/1>;rel="self"]"""))
        assertEquals("Some Author1", result.author)
        assertEquals("Some Title1", result.title)
        assertEquals(25.0, result.price)
        assertNotNull(result.launchDate)
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
        assertNotNull(result!!.key)
        assertNotNull(result.links)
        assertTrue(result.toString().contains("""links: [</api/book/v1/1>;rel="self"]"""))
        assertEquals("Some Author1", result.author)
        assertEquals("Some Title1", result.title)
        assertEquals(25.0, result.price)
        assertNotNull(result.launchDate)
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