package com.maned.wolf.kotlintwithtests.example.unittests.service

import com.maned.wolf.kotlintwithtests.example.exception.RequiredObjectIsNullException
import com.maned.wolf.kotlintwithtests.example.model.Person
import com.maned.wolf.kotlintwithtests.example.repository.PersonRepository
import com.maned.wolf.kotlintwithtests.example.service.PersonService
import com.maned.wolf.kotlintwithtests.example.unittests.service.mocks.MockPerson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension::class)
class PersonServiceTest {
    private var input: MockPerson? = null

    @InjectMocks
    private val service: PersonService? = null

    @Mock
    var repository: PersonRepository? = null

    @BeforeEach
    fun setupMock() {
        input = MockPerson()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testFindById() {
        val entity = input!!.mockEntity(1)
        `when`(repository?.findById(1)).thenReturn(Optional.of(entity))

        val result = service!!.findById(1)
        assertNotNull(result)
        assertNotNull(result.id)

        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun testCreate() {
        val entity = input!!.mockEntity(1)
        entity.id = null

        val persisted = entity.copy()
        persisted.id = 1

        `when`(repository?.save(entity)).thenReturn(persisted)


        val result = service!!.create(entity)
        assertNotNull(result)
        assertNotNull(result.id)

        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun testCreateWithNullPerson() {
        val exception: Exception = assertThrows(
            RequiredObjectIsNullException::class.java
        ) {service!!.create(null)}

        val expectedMessage = "It is not allowed to persist a null object"
        val actualMessage = exception.message
        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun testUpdate() {

        val entity = input!!.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1

        `when`(repository?.findById(1)).thenReturn(Optional.of(entity))
        `when`(repository?.save(entity)).thenReturn(persisted)

        val result = service!!.update(entity)
        assertNotNull(result)
        assertNotNull(result.id)

        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun testUpdateWithNullPerson() {
        val exception: Exception = assertThrows(
            RequiredObjectIsNullException::class.java
        ) {service!!.update(null)}

        val expectedMessage = "It is not allowed to persist a null object"
        val actualMessage = exception.message
        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun testDelete() {
        val entity = input!!.mockEntity(1)
        `when`(repository?.findById(1)).thenReturn(Optional.of(entity))

        service!!.delete(1)
    }

    @Test
    fun testFindAll() {
        val list: List<Person> = input!!.mockEntityList()
        `when`(repository?.findAll()).thenReturn(list)

        val persons = service!!.findAll()

        assertNotNull(persons)
        assertEquals(14, persons.size)

        val personOne = persons[1]

        assertNotNull(personOne)
        assertNotNull(personOne.id)

        assertEquals("Address Test1", personOne.address)
        assertEquals("First Name Test1", personOne.firstName)
        assertEquals("Last Name Test1", personOne.lastName)
        assertEquals("Female", personOne.gender)

        val personFour = persons[4]

        assertNotNull(personFour)
        assertNotNull(personFour.id)

        assertEquals("Address Test4", personFour.address)
        assertEquals("First Name Test4", personFour.firstName)
        assertEquals("Last Name Test4", personFour.lastName)
        assertEquals("Male", personFour.gender)

        val personSeven = persons[7]

        assertNotNull(personSeven)
        assertNotNull(personSeven.id)

        assertEquals("Address Test7", personSeven.address)
        assertEquals("First Name Test7", personSeven.firstName)
        assertEquals("Last Name Test7", personSeven.lastName)
        assertEquals("Female", personSeven.gender)
    }
}