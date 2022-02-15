package br.com.erudio.unittests.mockito.services

import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.exception.RequiredObjectIsNullException
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import br.com.erudio.services.PersonServices
import br.com.erudio.unittests.mapper.mocks.MockPerson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.*
import java.util.*
import java.util.stream.Collectors


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension::class)
class PersonServicesTest {

    // https://stackoverflow.com/questions/52139619/simulation-of-service-using-mockito-2-leads-to-stubbing-error

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
        val page: Page<Person> = PageImpl(list)

        val pageable: Pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"))

        `when`(repository!!.findAll(pageable)).thenReturn(page)
        val searchPage = service!!.findAll(pageable)!!.content

        val persons: List<PersonVO> = searchPage.stream().collect(Collectors.toList<PersonVO>()) as List<PersonVO>

        assertNotNull(persons)
        assertEquals(14, persons.size)
        val personOne = persons[1]
        assertNotNull(personOne)
        assertNotNull(personOne.key)
        assertNotNull(personOne.links)
        assertTrue(personOne.toString().contains("""links: [</api/person/v1/1>;rel="self"]"""))
        assertEquals("Address Test1", personOne.address)
        assertEquals("First Name Test1", personOne.firstName)
        assertEquals("Last Name Test1", personOne.lastName)
        assertEquals("Female", personOne.gender)
        val personFour = persons[4]
        assertNotNull(personFour)
        assertNotNull(personFour.key)
        assertNotNull(personFour.links)
        assertTrue(personFour.toString().contains("""links: [</api/person/v1/4>;rel="self"]"""))
        assertEquals("Address Test4", personFour.address)
        assertEquals("First Name Test4", personFour.firstName)
        assertEquals("Last Name Test4", personFour.lastName)
        assertEquals("Male", personFour.gender)
        val personSeven = persons[7]
        assertNotNull(personSeven)
        assertNotNull(personSeven.key)
        assertNotNull(personSeven.links)
        assertTrue(personSeven.toString().contains("""links: [</api/person/v1/7>;rel="self"]"""))
        assertEquals("Address Test7", personSeven.address)
        assertEquals("First Name Test7", personSeven.firstName)
        assertEquals("Last Name Test7", personSeven.lastName)
        assertEquals("Female", personSeven.gender)
    }

    @Test
    fun testFindPersonByName() {
        val person: Person = input!!.mockEntity()
        person.id = 1L
        val list: ArrayList<Person?> = ArrayList()
        list.add(person)

        val page: Page<Person?> = PageImpl(list)

        val pageable: Pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"))

        `when`(repository!!.findPersonByName("Person name", pageable)).thenReturn(page)


        val searchPage = service!!.findPersonByName("Person name", pageable)!!.content

        val persons: List<PersonVO> = searchPage.stream().collect(Collectors.toList<PersonVO>()) as List<PersonVO>


        val result = persons[0]

        assertNotNull(result)
        assertNotNull(result.key)
        assertEquals("Address Test0", result.address)
        assertEquals("First Name Test0", result.firstName)
        assertEquals("Last Name Test0", result.lastName)
        assertEquals("Male", result.gender)
        assertTrue(result.enabled)
    }

    @Test
    fun testFindById() {
        val person = input!!.mockEntity(1)
        person.id = 1L
        `when`(repository!!.findById(1L)).thenReturn(Optional.of(person))
        val result = service!!.findById(1L)
        assertNotNull(result)
        assertNotNull(result!!.key)
        assertNotNull(result.links)
        assertTrue(result.toString().contains("""links: [</api/person/v1/1>;rel="self"]"""))
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    @RepeatedTest(15)
    fun testCreate() {
        val entity = input!!.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1L

        val vo = input!!.mockVO(1)
        vo.key = 1L

        `when`(repository!!.save(entity)).thenReturn(persisted)
        val result = service!!.create(vo)

        assertNotNull(result)
        assertNotNull(result!!.key)
        assertNotNull(result.links)
        assertTrue(result.toString().contains("""links: [</api/person/v1/1>;rel="self"]"""))
        assertEquals("Address Test1", result.address)
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
        `when`(repository!!.findById(1L)).thenReturn(Optional.of(entity))
        `when`(repository!!.save(entity)).thenReturn(persisted)
        val result = service!!.update(vo)
        assertNotNull(result)
        assertNotNull(result!!.key)
        assertNotNull(result.links)
        assertTrue(result.toString().contains("""links: [</api/person/v1/1>;rel="self"]"""))
        assertEquals("Address Test1", result.address)
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
        assertNotNull(result.links)
        assertTrue(result.toString().contains("""links: [</api/person/v1/1>;rel="self"]"""))
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun testDelete() {
        val person = input!!.mockEntity(1)
        person.id = 1L
        `when`(repository!!.findById(1L)).thenReturn(Optional.of(person))
        service!!.delete(1L)
    }
}