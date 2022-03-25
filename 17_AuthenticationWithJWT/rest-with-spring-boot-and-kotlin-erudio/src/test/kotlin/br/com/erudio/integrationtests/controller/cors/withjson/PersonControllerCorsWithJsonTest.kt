package br.com.erudio.integrationtests.controller.cors.withjson

import br.com.erudio.integrationtests.TestConfigs
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.erudio.integrationtests.vo.PersonVO
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
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

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: ObjectMapper
    private lateinit var person: PersonVO

    @BeforeAll
    fun setup() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        person = PersonVO()
    }

    @Test
    @Order(1)
    @Throws(
        JsonMappingException::class,
        JsonProcessingException::class
    )
    fun testCreate() {
        mockPerson()
        specification = RequestSpecBuilder()
            .addHeader(
                        TestConfigs.HEADER_PARAM_ORIGIN,
              "https://erudio.com.br")
                .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
                .addFilter(RequestLoggingFilter(LogDetail.ALL))
                .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

        val content = RestAssured.given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .`when`()
                .post()
            .then()
                .statusCode(200)
                    .extract()
                    .body()
                    .asString()
        val createdPerson = objectMapper.readValue(
            content,
            PersonVO::class.java
        )
        person = createdPerson
        assertNotNull(createdPerson.id)
        assertNotNull(createdPerson.firstName)
        assertNotNull(createdPerson.lastName)
        assertNotNull(createdPerson.address)
        assertNotNull(createdPerson.gender)
        assertTrue(createdPerson.id > 0)
        assertEquals("Nelson", createdPerson.firstName)
        assertEquals("Piquet", createdPerson.lastName)
        assertEquals("Brasília, DF, Brasil", createdPerson.address)
        assertEquals("Male", createdPerson.gender)
    }

    @Test
    @Order(2)
    @Throws(
        JsonMappingException::class,
        JsonProcessingException::class
    )
    fun testCreateWithWrongOrigin() {
        mockPerson()
        specification = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, "https://semeru.com.br")
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
        val content = RestAssured.given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
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
    @Order(3)
    @Throws(
        JsonMappingException::class,
        JsonProcessingException::class
    )
    fun testFindById() {
        specification = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, "http://localhost:8080")
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
        val content = RestAssured.given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", person.id)
            .`when`()["{id}"]
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val foundPerson = objectMapper.readValue(
            content,
            PersonVO::class.java
        )
        assertNotNull(foundPerson.id)
        assertNotNull(foundPerson.firstName)
        assertNotNull(foundPerson.lastName)
        assertNotNull(foundPerson.address)
        assertNotNull(foundPerson.gender)
        assertEquals(foundPerson.id, person.id)
        assertEquals("Nelson", foundPerson.firstName)
        assertEquals("Piquet", foundPerson.lastName)
        assertEquals("Brasília, DF, Brasil", foundPerson.address)
        assertEquals("Male", foundPerson.gender)
    }

    @Test
    @Order(4)
    @Throws(
        JsonMappingException::class,
        JsonProcessingException::class
    )
    fun testFindByIdWithWrongOrigin() {
        specification = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, "https://semeru.com.br")
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
        val content = RestAssured.given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
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
        person.firstName = "Nelson"
        person.lastName = "Piquet"
        person.address = "Brasília, DF, Brasil"
        person.gender = "Male"
    }
}