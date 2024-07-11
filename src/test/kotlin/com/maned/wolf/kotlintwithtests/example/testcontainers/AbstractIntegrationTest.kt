package com.maned.wolf.kotlintwithtests.example.testcontainers

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.lifecycle.Startables
import java.util.stream.Stream

@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
open class AbstractIntegrationTest {
    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            startContainers()
            val environment = applicationContext.environment
            val testcontainers = MapPropertySource(
                "testcontainers", createConnectionConfiguration())
            environment.propertySources.addFirst(testcontainers)
        }

        companion object {
            private val postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:latest")

            private fun startContainers() {
                Startables.deepStart(Stream.of(postgres)).join()
            }

            private fun createConnectionConfiguration(): MutableMap<String, Any> {
                return java.util.Map.of(
                    "spring.datasource.url", postgres.jdbcUrl,
                    "spring.datasource.username", postgres.username,
                    "spring.datasource.password", postgres.password,
                )
            }
        }

    }
}