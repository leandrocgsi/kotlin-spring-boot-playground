package br.com.erudio.integrationtests.controller.withyaml

import br.com.erudio.configs.TestsConfig
import br.com.erudio.integrationtests.controller.withyaml.mapper.YMLMapper
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.erudio.integrationtests.vo.AccountCredentialsVO
import br.com.erudio.integrationtests.vo.PersonVO
import br.com.erudio.integrationtests.vo.TokenVO
import br.com.erudio.integrationtests.vo.wrappers.WrapperPersonVO
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.EncoderConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerYamlTest : AbstractIntegrationTest() {

    private var specification: RequestSpecification? = null
    private var objectMapper: YMLMapper? = null
    private var person: PersonVO? = null

    @BeforeAll
    fun setup() {
        objectMapper = YMLMapper()
        person = PersonVO()
    }

    @Test
    @Order(1)
    fun authorization() {
        val user = AccountCredentialsVO()
        user.username = "leandro"
        user.password = "admin123"

        val token = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestsConfig.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .basePath("/auth/signin")
            .port(TestsConfig.SERVER_PORT)
            .contentType(TestsConfig.CONTENT_TYPE_YML)
            .body(user, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java, objectMapper)
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
        val createdPerson = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestsConfig.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_YML)
            .body(person, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)
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
        val updatedPerson: PersonVO = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestsConfig.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_YML)
            .body(person, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)
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
        val patchedPerson = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestsConfig.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_YML)
            .pathParam("id", person!!.id)
            .`when`()
            .patch("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)

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
        val foundPerson: PersonVO = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestsConfig.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_YML)
            .pathParam("id", person!!.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)

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
        given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestsConfig.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_YML)
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
        val wrapper = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestsConfig.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_YML)
            .queryParams("page", 6, "limit", 10, "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(WrapperPersonVO::class.java, objectMapper)

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
        given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestsConfig.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specificationWithoutToken)
            .contentType(TestsConfig.CONTENT_TYPE_YML) //.queryParams("page", 6 , "limit", 10, "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(403)
    }

    @Test
    @Order(9)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindPersonByName() {
        val wrapper = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestsConfig.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_YML)
            .pathParam("firstName", "Leandro")
            .queryParams("page", 0, "limit", 5, "direction", "asc")
            .`when`()["findPersonByName/{firstName}"]
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(WrapperPersonVO::class.java, objectMapper)

        val persons = wrapper.embedded!!.persons
        val foundPersonOne = persons!![0]

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