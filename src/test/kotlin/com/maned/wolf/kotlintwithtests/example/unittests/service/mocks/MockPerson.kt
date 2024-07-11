package com.maned.wolf.kotlintwithtests.example.unittests.service.mocks

import com.maned.wolf.kotlintwithtests.example.model.Person

class MockPerson {

    fun mockEntityList(): ArrayList<Person> {
        val persons: ArrayList<Person> = ArrayList<Person>()
        for (i in 0..13) {
            persons.add(mockEntity(i))
        }
        return persons
    }

    fun mockEntity(number: Int): Person {
        val person = Person()
        person.firstName = "First Name Test${number}"
        person.lastName = "Last Name Test${number}"
        person.address = "Address Test${number}"
        person.gender = if (number % 2 == 0) "Male" else "Female"
        person.id = number.toLong()
        return person
    }
}