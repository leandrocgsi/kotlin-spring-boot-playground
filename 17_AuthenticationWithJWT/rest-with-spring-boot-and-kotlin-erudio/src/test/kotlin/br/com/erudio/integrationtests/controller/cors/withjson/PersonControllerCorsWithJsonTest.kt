package br.com.erudio.integrationtests.controller.cors.withjson

import br.com.erudio.configs.TestsConfig
import br.com.erudio.data.vo.v1.security.AccountCredentialsVO
import br.com.erudio.data.vo.v1.security.TokenVO
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.erudio.integrationtests.vo.PersonVO
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
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerCorsWithJsonTest : AbstractIntegrationTest() {

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
            .contentType(TestsConfig.CONTENT_TYPE_JSON)
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
    @Throws(
        JsonMappingException::class,
        JsonProcessingException::class
    )
    fun testCreate() {
        mockPerson()

        val content = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_JSON)
                .header(TestsConfig.HEADER_PARAM_ORIGIN, "https://erudio.com.br")
                .body(person)
                .`when`()
                .post()
            .then()
                .statusCode(200)
                    .extract()
                    .body()
                    .asString()
        val createdPerson = objectMapper!!.readValue(
            content,
            PersonVO::class.java
        )
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
    }

    @Test
    @Order(3)
    @Throws(
        JsonMappingException::class,
        JsonProcessingException::class
    )
    fun testCreateWithWrongOrigin() {
        mockPerson()

        val content = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_JSON)
                .header(TestsConfig.HEADER_PARAM_ORIGIN, "https://semeru.com.br")
                .body(person)
            .`when`()
                .post()
            .then()
                .statusCode(403)
            .extract()
                .body()
                .asString()
        assertNotNull(content)
        assertEquals("Invalid CORS request", content)
    }

    @Test
    @Order(4)
    @Throws(
        JsonMappingException::class,
        JsonProcessingException::class
    )
    fun testFindById() {

        val content = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_JSON)
            .header(TestsConfig.HEADER_PARAM_ORIGIN, "http://localhost:8080")
            .pathParam("id", person!!.id)
            .`when`()["{id}"]
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val foundPerson = objectMapper!!.readValue(
            content,
            PersonVO::class.java
        )
        assertNotNull(foundPerson.id)
        assertNotNull(foundPerson.firstName)
        assertNotNull(foundPerson.lastName)
        assertNotNull(foundPerson.address)
        assertNotNull(foundPerson.gender)
        assertEquals(foundPerson.id, person!!.id)
        assertEquals("Richard", foundPerson.firstName)
        assertEquals("Stallman", foundPerson.lastName)
        assertEquals("New York City, New York, US", foundPerson.address)
        assertEquals("Male", foundPerson.gender)
    }

    @Test
    @Order(4)
    @Throws(
        JsonMappingException::class,
        JsonProcessingException::class
    )
    fun testFindByIdWithWrongOrigin() {

        val content = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_JSON)
            .header(TestsConfig.HEADER_PARAM_ORIGIN, "https://semeru.com.br")
            .pathParam("id", 10)
            .`when`()["{id}"]
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()
        assertNotNull(content)
        assertEquals("Invalid CORS request", content)
    }

    private fun mockPerson() {
        person!!.firstName = "Richard"
        person!!.lastName = "Stallman"
        person!!.address = "New York City, New York, US"
        person!!.gender = "Male"
    }
}