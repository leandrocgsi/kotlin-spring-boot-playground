package br.com.erudio.unittests.mockito.services

import br.com.erudio.exception.RequiredObjectIsNullException
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import br.com.erudio.services.PersonServices
import br.com.erudio.unittests.mapper.mocks.MockPerson

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mockito.`when`

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension::class)
class PersonServicesTest {

    private var input: MockPerson? = null

    @InjectMocks
    private val service: PersonServices? = null

    @Mock
    var repository: PersonRepository? = null

    @BeforeEach
    fun setupMock() {
        input = MockPerson()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testFindAll() {
        val list: List<Person> = input!!.mockEntityList()
        Mockito.`when`(repository!!.findAll()).thenReturn(list)
        val persons = service!!.findAll()
        assertNotNull(persons)
        assertEquals(14, persons!!.size)
        val personOne = persons[1]
        assertNotNull(personOne)
        assertNotNull(personOne.key)
        assertNotNull(personOne.links)
        assertTrue(personOne.toString().contains("""links: [</api/person/v1/1>;rel="self"]"""))
        assertEquals("Addres Test1", personOne.address)
        assertEquals("First Name Test1", personOne.firstName)
        assertEquals("Last Name Test1", personOne.lastName)
        assertEquals("Female", personOne.gender)
        val personFour = persons[4]
        assertNotNull(personFour)
        assertNotNull(personFour.key)
        assertNotNull(personFour.links)
        assertTrue(personFour.toString().contains("links: [</api/person/v1/4>;rel=\"self\"]"))
        assertEquals("Addres Test4", personFour.address)
        assertEquals("First Name Test4", personFour.firstName)
        assertEquals("Last Name Test4", personFour.lastName)
        assertEquals("Male", personFour.gender)
        val personSeven = persons[7]
        assertNotNull(personSeven)
        assertNotNull(personSeven.key)
        assertNotNull(personSeven.links)
        assertTrue(personSeven.toString().contains("links: [</api/person/v1/7>;rel=\"self\"]"))
        assertEquals("Addres Test7", personSeven.address)
        assertEquals("First Name Test7", personSeven.firstName)
        assertEquals("Last Name Test7", personSeven.lastName)
        assertEquals("Female", personSeven.gender)
    }

    @Test
    fun testFindById() {
        val person = input!!.mockEntity(1)
        person.id = 1L
        Mockito.`when`(repository!!.findById(1L)).thenReturn(Optional.of(person))
        val result = service!!.findById(1L)
        assertNotNull(result)
        assertNotNull(result!!.key)
        assertNotNull(result.links)
        assertTrue(result.toString().contains("""links: [</api/person/v1/1>;rel="self"]"""))
        assertEquals("Addres Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun testCreate() {
        val entity = input!!.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1L

        val vo = input!!.mockVO(1)
        vo.key = 1L

        Mockito.`when`(repository!!.save(entity)).thenReturn(persisted)
        val result = service!!.create(vo)

        assertNotNull(result)
        assertNotNull(result!!.key)
        assertNotNull(result.links)
        assertTrue(result.toString().contains("""links: [</api/person/v1/1>;rel="self"]"""))
        assertEquals("Addres Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun testCreateWithNullPerson() {
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
        Mockito.`when`(repository!!.findById(1L)).thenReturn(Optional.of(entity))
        Mockito.`when`(repository!!.save(entity)).thenReturn(persisted)
        val result = service!!.update(vo)
        assertNotNull(result)
        assertNotNull(result!!.key)
        assertNotNull(result.links)
        assertTrue(result.toString().contains("""links: [</api/person/v1/1>;rel="self"]"""))
        assertEquals("Addres Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun testUpdateWithNullPerson() {
        val exception: Exception = assertThrows(
            RequiredObjectIsNullException::class.java
        ) { service!!.update(null) }
        val expectedMessage = "It is not allowed to persist a null object!"
        val actualMessage = exception.message
        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun disablePerson() {
        val person = input!!.mockEntity(1)
        person.id = 1L
        person.enabled = false
        `when`(repository!!.findById(1L)).thenReturn(Optional.of(person))
        val result = service!!.disablePerson(1L)
        assertNotNull(result)
        assertNotNull(result!!.key)
        assertNotNull(result!!.links)
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"))
        assertEquals("Addres Test1", result!!.address)
        assertEquals("First Name Test1", result!!.firstName)
        assertEquals("Last Name Test1", result!!.lastName)
        assertEquals("Female", result!!.gender)
    }

    @Test
    fun testDelete() {
        val person = input!!.mockEntity(1)
        person.id = 1L
        Mockito.`when`(repository!!.findById(1L)).thenReturn(Optional.of(person))
        service!!.delete(1L)
    }
}