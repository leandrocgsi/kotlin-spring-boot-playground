package br.com.erudio.integrationtests.controller.withxml

import br.com.erudio.configs.TestsConfig
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.erudio.integrationtests.vo.AccountCredentialsVO
import br.com.erudio.integrationtests.vo.PersonVO
import br.com.erudio.integrationtests.vo.TokenVO
import br.com.erudio.integrationtests.vo.wrappers.WrapperPersonVO
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerXmlTest : AbstractIntegrationTest() {

    private var specification: RequestSpecification? = null
    private var objectMapper: ObjectMapper? = null
    private var person: PersonVO? = null

    @BeforeAll
    fun setup() {
        objectMapper = ObjectMapper()
        objectMapper!!.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        person = PersonVO()
    }

    @Test
    @Order(1)
    fun authorization() {
        val user = AccountCredentialsVO()
        user.username = "leandro"
        user.password = "admin123"
        val token = given()
            .basePath("/auth/signin")
            .port(TestsConfig.SERVER_PORT)
            .contentType(TestsConfig.CONTENT_TYPE_XML)
            .body(user)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java)
            .accessToken

        specification = RequestSpecBuilder()
            .addHeader(TestsConfig.HEADER_PARAM_AUTHORIZATION, "Bearer $token")
            .setBasePath("/api/person/v1")
            .setPort(TestsConfig.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
    }

    @Test
    @Order(2)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testCreate() {
        mockPerson()
        val content: String = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_XML)
            .body(person)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val createdPerson = objectMapper!!.readValue(content, PersonVO::class.java)
        person = createdPerson
        assertNotNull(createdPerson.id)
        assertNotNull(createdPerson.firstName)
        assertNotNull(createdPerson.lastName)
        assertNotNull(createdPerson.address)
        assertNotNull(createdPerson.gender)
        assertTrue(createdPerson.id!! > 0)
        assertEquals("Richard", createdPerson.firstName)
        assertEquals("Stallman", createdPerson.lastName)
        assertEquals("New York City, New York, US", createdPerson.address)
        assertEquals("Male", createdPerson.gender)
        assertEquals(true, createdPerson.enabled)
    }

    @Test
    @Order(3)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testUpdate() {
        person!!.lastName = "Matthew Stallman"
        val content: String = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_XML)
            .body(person)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val updatedPerson = objectMapper!!.readValue(content, PersonVO::class.java)
        assertNotNull(updatedPerson.id)
        assertNotNull(updatedPerson.firstName)
        assertNotNull(updatedPerson.lastName)
        assertNotNull(updatedPerson.address)
        assertNotNull(updatedPerson.gender)
        assertEquals(updatedPerson.id, person!!.id)
        assertEquals("Richard", updatedPerson.firstName)
        assertEquals("Matthew Stallman", updatedPerson.lastName)
        assertEquals("New York City, New York, US", updatedPerson.address)
        assertEquals("Male", updatedPerson.gender)
        assertEquals(true, updatedPerson.enabled)
    }

    @Test
    @Order(4)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testDisablePerson() {

        person!!.enabled = false

        val content: String = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_XML)
                .pathParam("id", person!!.id)
                .`when`()
                .patch("{id}")
            .then()
                .statusCode(200)
                        .extract()
                        .body()
                            .asString()

        val patchedPerson: PersonVO = objectMapper!!.readValue(content, PersonVO::class.java)
        assertNotNull(patchedPerson.id)
        assertNotNull(patchedPerson.firstName)
        assertNotNull(patchedPerson.lastName)
        assertNotNull(patchedPerson.address)
        assertNotNull(patchedPerson.gender)
        assertEquals(patchedPerson.id, person!!.id)
        assertEquals("Richard", patchedPerson.firstName)
        assertEquals("Matthew Stallman", patchedPerson.lastName)
        assertEquals("New York City, New York, US", patchedPerson.address)
        assertEquals("Male", patchedPerson.gender)
        assertEquals(false, patchedPerson.enabled)
    }

    @Test
    @Order(5)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindById() {
        val content: String = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_XML)
            .pathParam("id", person!!.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val foundPerson = objectMapper!!.readValue(content, PersonVO::class.java)
        assertNotNull(foundPerson.id)
        assertNotNull(foundPerson.firstName)
        assertNotNull(foundPerson.lastName)
        assertNotNull(foundPerson.address)
        assertNotNull(foundPerson.gender)
        assertEquals(foundPerson.id, person!!.id)
        assertEquals("Richard", foundPerson.firstName)
        assertEquals("Matthew Stallman", foundPerson.lastName)
        assertEquals("New York City, New York, US", foundPerson.address)
        assertEquals("Male", foundPerson.gender)
        assertEquals(false, foundPerson.enabled)
    }

    @Test
    @Order(6)
    fun testDelete() {
        given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_XML)
            .pathParam("id", person!!.id)
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)
    }

    @Test
    @Order(7)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindAll() {
        val content = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_XML)
            .queryParams("page", 6 , "limit", 10, "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val wrapper = objectMapper!!.readValue(content, WrapperPersonVO::class.java)
        val people = wrapper.embedded!!.persons

        val foundPersonOne = people?.get(0)
        assertNotNull(foundPersonOne!!.id)
        assertNotNull(foundPersonOne.firstName)
        assertNotNull(foundPersonOne.lastName)
        assertNotNull(foundPersonOne.address)
        assertNotNull(foundPersonOne.gender)
        assertEquals(964, foundPersonOne.id)
        assertEquals("Ardath", foundPersonOne.firstName)
        assertEquals("Leckenby", foundPersonOne.lastName)
        assertEquals("9 Chive Trail", foundPersonOne.address)
        assertEquals("Female", foundPersonOne.gender)
        assertEquals(true, foundPersonOne.enabled)

        val foundPersonSeven = people[6]
        assertNotNull(foundPersonSeven.id)
        assertNotNull(foundPersonSeven.firstName)
        assertNotNull(foundPersonSeven.lastName)
        assertNotNull(foundPersonSeven.address)
        assertNotNull(foundPersonSeven.gender)
        assertEquals(189, foundPersonSeven.id)
        assertEquals("Arlena", foundPersonSeven.firstName)
        assertEquals("Wagenen", foundPersonSeven.lastName)
        assertEquals("1 Spaight Parkway", foundPersonSeven.address)
        assertEquals("Female", foundPersonSeven.gender)
    }

    @Test
    @Order(8)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindAllWithoutToken() {
        val specificationWithoutToken: RequestSpecification = RequestSpecBuilder()
            .setBasePath("/api/person/v1")
            .setPort(TestsConfig.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
        given().spec(specificationWithoutToken)
            .contentType(TestsConfig.CONTENT_TYPE_XML) //.queryParams("page", 6 , "limit", 10, "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(403)
    }

    @Test
    @Order(9)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindPersonByName() {
        val content = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_XML)
            .pathParam("firstName", "Leandro")
            .queryParams("page", 0, "limit", 5, "direction", "asc")
            .`when`()["findPersonByName/{firstName}"]
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val wrapper = objectMapper!!.readValue(content, WrapperPersonVO::class.java)
        val people = wrapper.embedded!!.persons

        val foundPersonOne = people!![0]

        assertNotNull(foundPersonOne.id)
        assertNotNull(foundPersonOne.firstName)
        assertNotNull(foundPersonOne.lastName)
        assertNotNull(foundPersonOne.address)
        assertNotNull(foundPersonOne.gender)

        assertEquals(1, foundPersonOne.id)
        assertEquals("Leandro", foundPersonOne.firstName)
        assertEquals("Costa", foundPersonOne.lastName)
        assertEquals("Uberlândia - Minas Gerais - Brasil", foundPersonOne.address)
        assertEquals("Male", foundPersonOne.gender)
        assertEquals(true, foundPersonOne.enabled)
    }

    private fun mockPerson() {
        person!!.firstName = "Richard"
        person!!.lastName = "Stallman"
        person!!.address = "New York City, New York, US"
        person!!.gender = "Male"
        person!!.enabled = true
    }
}