package br.com.erudio.integrationtests.swagger

import br.com.erudio.configs.TestsConfig
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest() : AbstractIntegrationTest() {
    @Test
    fun shouldDisplaySwaggerUiPage() {
        val contentAsString = RestAssured
            .given()
                .basePath("/swagger-ui/index.html")
                .port(TestsConfig.SERVER_PORT)
                .`when`()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                .body()
                    .asString()
        Assertions.assertTrue(contentAsString.contains("Swagger UI"))
    }
}