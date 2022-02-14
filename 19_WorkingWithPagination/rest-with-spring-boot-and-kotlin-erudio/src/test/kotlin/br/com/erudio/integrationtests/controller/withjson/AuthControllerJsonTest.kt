package br.com.erudio.integrationtests.controller.withjson


import br.com.erudio.configs.TestsConfig
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.erudio.integrationtests.vo.AccountCredentialsVO
import br.com.erudio.integrationtests.vo.TokenVO
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation::class)
internal class AuthControllerJsonTest : AbstractIntegrationTest() {

    @Test
    @Order(1)
    fun testSignin() {
        val user = AccountCredentialsVO()
        user.username = "leandro"
        user.password = "admin123"

        tokenVO = given()
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

        assertNotNull(tokenVO!!.accessToken)
        assertNotNull(tokenVO!!.refreshToken)
    }

    @Test
    @Order(2)
    fun testRefresh() {
        val newTokenVO = given()
            .basePath("/auth/refresh")
            .port(TestsConfig.SERVER_PORT)
            .contentType(TestsConfig.CONTENT_TYPE_JSON)
            .pathParam("username", tokenVO!!.username)
            .header(TestsConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO!!.refreshToken)
            .`when`()
            .put("{username}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java)

        assertNotNull(newTokenVO!!.accessToken)
        assertNotNull(newTokenVO.refreshToken)
    }

    companion object {
        private var tokenVO: TokenVO? = null
    }
}