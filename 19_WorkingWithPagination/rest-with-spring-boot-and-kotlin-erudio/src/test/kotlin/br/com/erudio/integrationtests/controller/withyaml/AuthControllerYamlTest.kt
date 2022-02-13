package br.com.erudio.integrationtests.controller.withyaml

import io.restassured.RestAssured.given
import org.junit.jupiter.api.Assertions.assertNotNull

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.boot.test.context.SpringBootTest

import com.fasterxml.jackson.core.JsonProcessingException

import br.com.erudio.configs.TestsConfig
import br.com.erudio.integrationtests.controller.withyaml.mapper.YMLMapper
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.erudio.integrationtests.vo.AccountCredentialsVO
import br.com.erudio.integrationtests.vo.TokenVO
import io.restassured.config.EncoderConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import org.junit.jupiter.api.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerYamlTest : AbstractIntegrationTest() {

    private var objectMapper: YMLMapper? = null
    private var tokenVO: TokenVO? = null

    @BeforeAll
    fun setup() {
        objectMapper = YMLMapper()
    }

    @Test
    @Order(1)
    @Throws(JsonProcessingException::class)
    fun testSignin() {
        val user = AccountCredentialsVO()
        user.username = "leandro"
        user.password = "admin123"

        tokenVO = given()
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
            .accept(TestsConfig.CONTENT_TYPE_YML)
            .body(user, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java, objectMapper)

        assertNotNull(tokenVO!!.accessToken)
        assertNotNull(tokenVO!!.refreshToken)
    }

    @Test
    @Order(2)
    fun testRefresh() {
        val newTokenVO: Any = given()
            .basePath("/auth/refresh")
            .port(TestsConfig.SERVER_PORT)
            .contentType(TestsConfig.CONTENT_TYPE_YML)
            .pathParam("username", tokenVO!!.username)
            .header(TestsConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO!!.refreshToken)
            .`when`()
            .put("{username}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`<Any>(TokenVO::class.java, objectMapper)

        assertNotNull(tokenVO!!.accessToken)
        assertNotNull(tokenVO!!.refreshToken)
    }
}