package com.maned.wolf.kotlintwithtests.example.integrationtests.controller

import com.maned.wolf.kotlintwithtests.example.config.TestsConfig
import com.maned.wolf.kotlintwithtests.example.model.Person
import com.maned.wolf.kotlintwithtests.example.testcontainers.AbstractIntegrationTest
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerTest : AbstractIntegrationTest() {
    private var specification: RequestSpecification? = null
    private var objectMapper: ObjectMapper? = null
    private var person: Person? = null

    @BeforeAll
    fun setup() {
        objectMapper = ObjectMapper()
        objectMapper!!.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        person = Person()
    }

    @Test
    @Order(1)
    fun postSetup() {
        specification = RequestSpecBuilder()
            .setBasePath("/person")
            .setPort(TestsConfig.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
    }

    @Test
    @Order(2)
    fun testCreate() {
        mockPerson()

        val content: String = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE)
            .body(person)
            .`when`()
                .post()
            .then()
                .statusCode(200)
                .extract()
                    .body()
                        .asString()

        val createdPerson = objectMapper!!.readValue(content, Person::class.java)

        person = createdPerson

        assertNotNull(createdPerson.id)
        assertNotNull(createdPerson.firstName)
        assertNotNull(createdPerson.lastName)
        assertNotNull(createdPerson.address)
        assertNotNull(createdPerson.gender)
        assertTrue(createdPerson.id!! > 0)

        assertEquals("Calango", createdPerson.firstName)
        assertEquals("Cerrado", createdPerson.lastName)
        assertEquals("Rua Ipiranga", createdPerson.address)
        assertEquals("Male", createdPerson.gender)
    }

    @Test
    @Order(3)
    fun testUpdate() {
        person!!.lastName = "Chapada"

        val content: String = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE)
            .body(person)
            .`when`()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val updatedPerson = objectMapper!!.readValue(content, Person::class.java)

        person = updatedPerson

        assertNotNull(updatedPerson.id)
        assertNotNull(updatedPerson.firstName)
        assertNotNull(updatedPerson.lastName)
        assertNotNull(updatedPerson.address)
        assertNotNull(updatedPerson.gender)
        assertTrue(updatedPerson.id!! > 0)

        assertEquals("Calango", updatedPerson.firstName)
        assertEquals("Chapada", updatedPerson.lastName)
        assertEquals("Rua Ipiranga", updatedPerson.address)
        assertEquals("Male", updatedPerson.gender)
    }

    @Test
    @Order(4)
    fun testFindById() {

        val content: String = given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE)
                .pathParams("id", person!!.id)
            .`when`()
                .get("{id}")
            .then()
                .statusCode(200)
                .extract()
                    .body()
                    .asString()

        val foundPerson = objectMapper!!.readValue(content, Person::class.java)

        person = foundPerson

        assertNotNull(foundPerson.id)
        assertNotNull(foundPerson.firstName)
        assertNotNull(foundPerson.lastName)
        assertNotNull(foundPerson.address)
        assertNotNull(foundPerson.gender)
        assertTrue(foundPerson.id!! > 0)

        assertEquals(person!!.id, foundPerson.id)
        assertEquals(person!!.firstName, foundPerson.firstName)
        assertEquals(person!!.lastName,foundPerson.lastName)
        assertEquals(person!!.address, foundPerson.address)
        assertEquals(person!!.gender, foundPerson.gender)
    }

    @Test
    @Order(5)
    fun testDelete () {

        given().spec(specification)
            .contentType(TestsConfig.CONTENT_TYPE)
            .pathParams("id", person!!.id)
            .`when`()
                .delete("{id}")
            .then()
            .statusCode(204)

    }

    private fun mockPerson() {
        person?.firstName = "Calango"
        person?.lastName = "Cerrado"
        person?.address = "Rua Ipiranga"
        person?.gender = "Male"
    }
}