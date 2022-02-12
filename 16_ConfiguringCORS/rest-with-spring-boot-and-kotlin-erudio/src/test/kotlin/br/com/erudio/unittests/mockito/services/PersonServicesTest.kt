package br.com.erudio.unittests.mockito.services

import br.com.erudio.exception.RequiredObjectIsNullException
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import br.com.erudio.services.PersonServices
import br.com.erudio.unittests.mapper.mocks.MockPerson
import org.junit.jupiter.api.Assertions
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
        Assertions.assertNotNull(persons)
        Assertions.assertEquals(14, persons!!.size)
        val personOne = persons[1]
        Assertions.assertNotNull(personOne)
        Assertions.assertNotNull(personOne.key)
        Assertions.assertNotNull(personOne.links)
        Assertions.assertTrue(personOne.toString().contains("""links: [</person/1>;rel="self"]"""))
        Assertions.assertEquals("Addres Test1", personOne.address)
        Assertions.assertEquals("First Name Test1", personOne.firstName)
        Assertions.assertEquals("Last Name Test1", personOne.lastName)
        Assertions.assertEquals("Female", personOne.gender)
        val personFour = persons[4]
        Assertions.assertNotNull(personFour)
        Assertions.assertNotNull(personFour.key)
        Assertions.assertNotNull(personFour.links)
        Assertions.assertTrue(personFour.toString().contains("links: [</person/4>;rel=\"self\"]"))
        Assertions.assertEquals("Addres Test4", personFour.address)
        Assertions.assertEquals("First Name Test4", personFour.firstName)
        Assertions.assertEquals("Last Name Test4", personFour.lastName)
        Assertions.assertEquals("Male", personFour.gender)
        val personSeven = persons[7]
        Assertions.assertNotNull(personSeven)
        Assertions.assertNotNull(personSeven.key)
        Assertions.assertNotNull(personSeven.links)
        Assertions.assertTrue(personSeven.toString().contains("links: [</person/7>;rel=\"self\"]"))
        Assertions.assertEquals("Addres Test7", personSeven.address)
        Assertions.assertEquals("First Name Test7", personSeven.firstName)
        Assertions.assertEquals("Last Name Test7", personSeven.lastName)
        Assertions.assertEquals("Female", personSeven.gender)
    }

    @Test
    fun testFindById() {
        val person = input!!.mockEntity(1)
        person.id = 1L
        Mockito.`when`(repository!!.findById(1L)).thenReturn(Optional.of(person))
        val result = service!!.findById(1L)
        Assertions.assertNotNull(result)
        Assertions.assertNotNull(result!!.key)
        Assertions.assertNotNull(result.links)
        Assertions.assertTrue(result.toString().contains("""links: [</person/1>;rel="self"]"""))
        Assertions.assertEquals("Addres Test1", result.address)
        Assertions.assertEquals("First Name Test1", result.firstName)
        Assertions.assertEquals("Last Name Test1", result.lastName)
        Assertions.assertEquals("Female", result.gender)
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

        Assertions.assertNotNull(result)
        Assertions.assertNotNull(result!!.key)
        Assertions.assertNotNull(result.links)
        Assertions.assertTrue(result.toString().contains("""links: [</person/1>;rel="self"]"""))
        Assertions.assertEquals("Addres Test1", result.address)
        Assertions.assertEquals("First Name Test1", result.firstName)
        Assertions.assertEquals("Last Name Test1", result.lastName)
        Assertions.assertEquals("Female", result.gender)
    }

    @Test
    fun testCreateWithNullPerson() {
        val exception: Exception = Assertions.assertThrows(
            RequiredObjectIsNullException::class.java
        ) { service!!.create(null) }
        val expectedMessage = "It is not allowed to persist a null object!"
        val actualMessage = exception.message
        Assertions.assertTrue(actualMessage!!.contains(expectedMessage))
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
        Assertions.assertNotNull(result)
        Assertions.assertNotNull(result!!.key)
        Assertions.assertNotNull(result.links)
        Assertions.assertTrue(result.toString().contains("""links: [</person/1>;rel="self"]"""))
        Assertions.assertEquals("Addres Test1", result.address)
        Assertions.assertEquals("First Name Test1", result.firstName)
        Assertions.assertEquals("Last Name Test1", result.lastName)
        Assertions.assertEquals("Female", result.gender)
    }

    @Test
    fun testUpdateWithNullPerson() {
        val exception: Exception = Assertions.assertThrows(
            RequiredObjectIsNullException::class.java
        ) { service!!.update(null) }
        val expectedMessage = "It is not allowed to persist a null object!"
        val actualMessage = exception.message
        Assertions.assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun testDelete() {
        val person = input!!.mockEntity(1)
        person.id = 1L
        Mockito.`when`(repository!!.findById(1L)).thenReturn(Optional.of(person))
        service!!.delete(1L)
    }
}