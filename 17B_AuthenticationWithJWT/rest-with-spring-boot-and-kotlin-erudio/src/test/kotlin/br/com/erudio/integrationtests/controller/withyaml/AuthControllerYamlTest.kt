package br.com.erudio.integrationtests.controller.withyaml

import br.com.erudio.integrationtests.TestConfigs
import br.com.erudio.integrationtests.controller.withyaml.mapper.YMLMapper
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.erudio.integrationtests.vo.AccountCredentialsVO
import br.com.erudio.integrationtests.vo.TokenVO
import com.fasterxml.jackson.core.JsonProcessingException
import io.restassured.RestAssured.given
import io.restassured.config.EncoderConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerYamlTest : AbstractIntegrationTest() {

    private lateinit var objectMapper: YMLMapper
    private lateinit var tokenVO: TokenVO

    @BeforeAll
    fun setup() {
        objectMapper = YMLMapper()
        tokenVO = TokenVO()
    }

    @Test
    @Order(1)
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
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .accept(TestConfigs.CONTENT_TYPE_YML)
            .body(user, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java, objectMapper)

        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }

    @Test
    @Order(2)
    fun testRefresh() {
        val newTokenVO: Any = given()
            .basePath("/auth/refresh")
            .port(TestConfigs.SERVER_PORT)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .pathParam("username", tokenVO.username)
            .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.refreshToken)
            .`when`()
            .put("{username}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`<Any>(TokenVO::class.java, objectMapper)

        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }
}