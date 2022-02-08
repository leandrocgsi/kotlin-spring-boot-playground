package br.com.erudio.services

import br.com.erudio.model.Person
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong

@Service
class PersonServices {

    private val counter = AtomicLong()

    fun create(person: Person): Person = person

    fun update(person: Person): Person = person

    fun delete(id: String) {}

    fun findById(id: String): Person {

        val person = Person()
        person.id = counter.incrementAndGet()
        person.firstName = "Leandro"
        person.lastName = "Costa"
        person.address = "Uberlândia - Minas Gerais -Brasil"
        person.gender = "Male"
        return person
    }

    fun findAll(): List<Person> {
        val persons: MutableList<Person> = ArrayList()
        for (i in 0..7) {
            val person = mockPerson(i)
            persons.add(person)
        }
        return persons
    }

    private fun mockPerson(i: Int): Person {

        val person = Person()
        person.id = counter.incrementAndGet()
        person.firstName = "Person name $i"
        person.lastName = "Last name $i"
        person.address = "Some address in Brasil $i"
        person.gender = "Male"
        return person
    }
}