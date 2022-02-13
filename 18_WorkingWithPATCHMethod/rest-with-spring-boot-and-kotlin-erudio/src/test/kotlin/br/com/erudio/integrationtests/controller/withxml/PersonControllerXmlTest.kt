package br.com.erudio.integrationtests.controller.withxml

import io.restassured.RestAssured.given
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue

import java.util.List

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.boot.test.context.SpringBootTest

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper

import br.com.erudio.configs.TestsConfig
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.erudio.integrationtests.vo.AccountCredentialsVO
import br.com.erudio.integrationtests.vo.PersonVO
import br.com.erudio.integrationtests.vo.TokenVO
import io.restassured.builder.RequestSpecBuilder
import io.restassured.common.mapper.TypeRef
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*

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
        user!!.username = "leandro"
        user!!.password = "admin123"
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
        assertTrue(createdPerson.id!!!! > 0)
        assertEquals("Richard", createdPerson.firstName)
        assertEquals("Stallman", createdPerson.lastName)
        assertEquals("New York City, New York, US", createdPerson.address)
        assertEquals("Male", createdPerson.gender)
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
    }

    @Test
    @Order(6)
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
    }

    @Test
    @Order(7)
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
    @Order(8)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindAll() {
        val content = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_XML)
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(object : TypeRef<List<PersonVO?>?>() {})

        val foundPersonOne = content?.get(0)
        assertNotNull(foundPersonOne!!.id)
        assertNotNull(foundPersonOne!!.firstName)
        assertNotNull(foundPersonOne!!.lastName)
        assertNotNull(foundPersonOne!!.address)
        assertNotNull(foundPersonOne!!.gender)
        assertEquals(1, foundPersonOne!!.id)
        assertEquals("Leandro", foundPersonOne!!.firstName)
        assertEquals("Costa", foundPersonOne!!.lastName)
        assertEquals("Uberlândia - Minas Gerais - Brasil", foundPersonOne!!.address)
        assertEquals("Male", foundPersonOne!!.gender)
        val foundPersonSix = content?.get(5)
        assertNotNull(foundPersonSix!!.id)
        assertNotNull(foundPersonSix!!.firstName)
        assertNotNull(foundPersonSix!!.lastName)
        assertNotNull(foundPersonSix!!.address)
        assertNotNull(foundPersonSix!!.gender)
        assertEquals(9, foundPersonSix!!.id)
        assertEquals("Marcos", foundPersonSix!!.firstName)
        assertEquals("Paulo", foundPersonSix!!.lastName)
        assertEquals("Patos de Minas - Minas Gerais - Brasil", foundPersonSix!!.address)
        assertEquals("Male", foundPersonSix!!.gender)
    }

    @Test
    @Order(9)
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

    private fun mockPerson() {
        person!!.firstName = "Richard"
        person!!.lastName = "Stallman"
        person!!.address = "New York City, New York, US"
        person!!.gender = "Male"
    }
}