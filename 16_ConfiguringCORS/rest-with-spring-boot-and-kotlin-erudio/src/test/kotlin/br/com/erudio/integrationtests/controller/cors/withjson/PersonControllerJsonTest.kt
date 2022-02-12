package br.com.erudio.integrationtests.controller.cors.withjson

import br.com.erudio.configs.TestsConfig
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
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PersonControllerJsonTest : AbstractIntegrationTest() {

    @Test
    @Order(1)
    @Throws(
        JsonMappingException::class,
        JsonProcessingException::class
    )
    fun testCreate() {
        mockPerson()
        specification = RequestSpecBuilder()
            .addHeader(TestsConfig.HEADER_PARAM_ORIGIN, "https://erudio.com.br")
                .setBasePath("/api/person/v1")
            .setPort(TestsConfig.SERVER_PORT)
                .addFilter(RequestLoggingFilter(LogDetail.ALL))
                .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
        
        val content = RestAssured.given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_JSON)
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
        Assertions.assertNotNull(createdPerson.id)
        Assertions.assertNotNull(createdPerson.firstName)
        Assertions.assertNotNull(createdPerson.lastName)
        Assertions.assertNotNull(createdPerson.address)
        Assertions.assertNotNull(createdPerson.gender)
        Assertions.assertTrue(createdPerson.id!! > 0)
        Assertions.assertEquals("Richard", createdPerson.firstName)
        Assertions.assertEquals("Stallman", createdPerson.lastName)
        Assertions.assertEquals("New York City, New York, US", createdPerson.address)
        Assertions.assertEquals("Male", createdPerson.gender)
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
            .addHeader(TestsConfig.HEADER_PARAM_ORIGIN, "https://semeru.com.br")
            .setBasePath("/api/person/v1")
            .setPort(TestsConfig.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
        val content = RestAssured.given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_JSON)
            .body(person)
            .`when`()
            .post()
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()
        Assertions.assertNotNull(content)
        Assertions.assertEquals("Invalid CORS request", content)
    }

    @Test
    @Order(3)
    @Throws(
        JsonMappingException::class,
        JsonProcessingException::class
    )
    fun testFindById() {
        specification = RequestSpecBuilder()
            .addHeader(TestsConfig.HEADER_PARAM_ORIGIN, "http://localhost:8080")
            .setBasePath("/api/person/v1")
            .setPort(TestsConfig.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
        val content = RestAssured.given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_JSON)
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
        Assertions.assertNotNull(foundPerson.id)
        Assertions.assertNotNull(foundPerson.firstName)
        Assertions.assertNotNull(foundPerson.lastName)
        Assertions.assertNotNull(foundPerson.address)
        Assertions.assertNotNull(foundPerson.gender)
        Assertions.assertEquals(foundPerson.id, person!!.id)
        Assertions.assertEquals("Richard", foundPerson.firstName)
        Assertions.assertEquals("Stallman", foundPerson.lastName)
        Assertions.assertEquals("New York City, New York, US", foundPerson.address)
        Assertions.assertEquals("Male", foundPerson.gender)
    }

    @Test
    @Order(4)
    @Throws(
        JsonMappingException::class,
        JsonProcessingException::class
    )
    fun testFindByIdWithWrongOrigin() {
        specification = RequestSpecBuilder()
            .addHeader(TestsConfig.HEADER_PARAM_ORIGIN, "https://semeru.com.br")
            .setBasePath("/api/person/v1")
            .setPort(TestsConfig.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
        val content = RestAssured.given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_JSON)
            .pathParam("id", 10)
            .`when`()["{id}"]
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()
        Assertions.assertNotNull(content)
        Assertions.assertEquals("Invalid CORS request", content)
    }

    private fun mockPerson() {
        person!!.firstName = "Richard"
        person!!.lastName = "Stallman"
        person!!.address = "New York City, New York, US"
        person!!.gender = "Male"
    }

    companion object {
        private var specification: RequestSpecification? = null
        private var objectMapper: ObjectMapper? = null
        private var person: PersonVO? = PersonVO()

        @BeforeAll
        fun setup() {
            objectMapper = ObjectMapper()
            objectMapper!!.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            //person = PersonVO()
        }
    }
}