package br.com.erudio.integrationtests.repository

import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import org.junit.Assert.assertFalse
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var repository: PersonRepository

    private lateinit var person: Person

    @BeforeAll
    fun setup() {
        person = Person()
    }

    @Test
    @Order(1)
    fun testFindPersonByName() {
        val pageable: Pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"))
        person = repository.findPersonByName("Ayrt", pageable).content[0]
        assertNotNull(person)
        assertNotNull(person.id)
        assertEquals("São Paulo", person.address)
        assertEquals("Ayrton", person.firstName)
        assertEquals("Senna", person.lastName)
        assertEquals("Male", person.gender)
        assertTrue(person.enabled)
    }

    @Test
    @Order(2)
    fun testDisablePersons() {
        val id = person.id
        repository.disablePersons(id)
        val result = repository.findById(id)
        person = result.get()
        assertNotNull(result)
        assertNotNull(person)
        assertNotNull(person.id)
        assertEquals("São Paulo", person.address)
        assertEquals("Ayrton", person.firstName)
        assertEquals("Senna", person.lastName)
        assertEquals("Male", person.gender)
        assertFalse(person.enabled)
    }
}