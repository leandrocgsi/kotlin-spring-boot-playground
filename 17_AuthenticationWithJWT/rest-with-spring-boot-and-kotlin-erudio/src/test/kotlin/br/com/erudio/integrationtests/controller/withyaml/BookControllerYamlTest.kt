package br.com.erudio.integrationtests.controller.withyaml

import io.restassured.RestAssured.given
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue

import java.util.Arrays
import java.util.Date
import java.util.List

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.boot.test.context.SpringBootTest

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException

import br.com.erudio.configs.TestsConfig
import br.com.erudio.integrationtests.controller.withyaml.mapper.YMLMapper
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.erudio.integrationtests.vo.AccountCredentialsVO
import br.com.erudio.integrationtests.vo.BookVO
import br.com.erudio.integrationtests.vo.TokenVO
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.EncoderConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookControllerYamlTest : AbstractIntegrationTest() {

    private var specification: RequestSpecification? = null
    private var objectMapper: YMLMapper? = null
    private var book: BookVO? = null

    @BeforeAll
    fun setup() {
        objectMapper = YMLMapper()
        book = BookVO()
    }

    @Test
    @Order(1)
    fun authorization() {
        val user = AccountCredentialsVO()
        user!!.username = "leandro"
        user!!.password = "admin123"
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
            .setBasePath("/api/book/v1")
            .setPort(TestsConfig.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
    }

    @Test
    @Order(2)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testCreate() {
        mockBook()
        book = given()
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
            .body(book, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(BookVO::class.java, objectMapper)
        assertNotNull(book!!.id)
        assertNotNull(book!!.title)
        assertNotNull(book!!.author)
        assertNotNull(book!!.price)
        assertTrue(book!!.id!!!! > 0)
        assertEquals("Docker Deep Dive", book!!.title)
        assertEquals("Nigel Poulton", book!!.author)
        assertEquals(55.99, book!!.price)
    }

    @Test
    @Order(3)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testUpdate() {
        book!!.title = "Docker Deep Dive - Updated"
        val bookUpdated: BookVO = given()
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
            .body(book, objectMapper)
            .`when`()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(BookVO::class.java, objectMapper)
        assertNotNull(bookUpdated!!.id)
        assertNotNull(bookUpdated!!.title)
        assertNotNull(bookUpdated!!.author)
        assertNotNull(bookUpdated!!.price)
        assertEquals(bookUpdated!!.id, book!!.id)
        assertEquals("Docker Deep Dive - Updated", bookUpdated!!.title)
        assertEquals("Nigel Poulton", bookUpdated!!.author)
        assertEquals(55.99, bookUpdated!!.price)
    }

    @Test
    @Order(4)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindById() {
        val foundBook = given()
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
            .pathParam("id", book!!.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(BookVO::class.java, objectMapper)

        assertNotNull(foundBook!!.id)
        assertNotNull(foundBook!!.title)
        assertNotNull(foundBook!!.author)
        assertNotNull(foundBook!!.price)
        assertEquals(foundBook!!.id, book!!.id)
        assertEquals("Docker Deep Dive - Updated", foundBook!!.title)
        assertEquals("Nigel Poulton", foundBook!!.author)
        assertEquals(55.99, foundBook!!.price)
    }

    @Test
    @Order(5)
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
            .pathParam("id", book!!.id)
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)
    }

    @Test
    @Order(6)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindAll() {
        val response = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestsConfig.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE_YML) //.queryParams("page", 0 , "limit", 5, "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(Array<BookVO>::class.java, objectMapper)

        val content: MutableList<BookVO> = Arrays.asList(*response)
        val foundBookOne: BookVO = content[0]
        assertNotNull(foundBookOne!!.id)
        assertNotNull(foundBookOne!!.title)
        assertNotNull(foundBookOne!!.author)
        assertNotNull(foundBookOne!!.price)
        assertTrue(foundBookOne!!.id!! > 0)
        assertEquals("Working effectively with legacy code", foundBookOne!!.title)
        assertEquals("Michael C. Feathers", foundBookOne!!.author)
        assertEquals(49.00, foundBookOne!!.price)
        val foundBookFive: BookVO = content[4]
        assertNotNull(foundBookFive!!.id)
        assertNotNull(foundBookFive!!.title)
        assertNotNull(foundBookFive!!.author)
        assertNotNull(foundBookFive!!.price)
        assertTrue(foundBookFive!!.id!! > 0)
        assertEquals("Code complete", foundBookFive!!.title)
        assertEquals("Steve McConnell", foundBookFive!!.author)
        assertEquals(58.0, foundBookFive!!.price)
    }

    private fun mockBook() {
        book!!.title = "Docker Deep Dive"
        book!!.author = "Nigel Poulton"
        book!!.price = (java.lang.Double.valueOf(55.99))
        book!!.launchDate = Date()
    }
}