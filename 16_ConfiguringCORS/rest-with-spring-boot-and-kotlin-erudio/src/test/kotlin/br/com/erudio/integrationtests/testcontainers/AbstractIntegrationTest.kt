package br.com.erudio.integrationtests.testcontainers

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.lifecycle.Startables
import java.util.stream.Stream

@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
open class AbstractIntegrationTest {
    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            startContainers()
            val environment = applicationContext.environment
            val testcontainers = MapPropertySource(
                                "testcontainers",  createConnectionConfiguration() as Map<String, String>
                                 )
            environment.propertySources.addFirst(testcontainers)
        }

        companion object {
            var mysql: MySQLContainer<*> = MySQLContainer("mysql:8.0.28")
            private fun startContainers() {
                Startables.deepStart(Stream.of(mysql)).join()
            }

            private fun createConnectionConfiguration(): Map<String, String> {
                return java.util.Map.of(
                    "spring.datasource.url", mysql.getJdbcUrl(),
                    "spring.datasource.username", mysql.getUsername(),
                    "spring.datasource.password", mysql.getPassword()
                )
            }
        }
    }
}