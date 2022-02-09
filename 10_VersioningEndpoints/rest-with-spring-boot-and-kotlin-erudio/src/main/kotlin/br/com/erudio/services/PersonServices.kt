package br.com.erudio.services

import br.com.erudio.data.vo.v2.PersonVOV2
import br.com.erudio.exception.ResourceNotFoundException
import br.com.erudio.mapper.custom.PersonConverter
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.function.Supplier


@Service
class PersonServices {

    @Autowired
    private lateinit  var repository: PersonRepository

    @Autowired
    private lateinit  var converter: PersonConverter

    fun create(person: Person): Person {
        return repository.save(person)
    }

    fun createV2(person: PersonVOV2?): PersonVOV2? {
        val entity = converter.convertVOToEntity(person!!)
        return converter.convertEntityToVO(repository.save(entity))
    }

    fun findAll(): List<Person> {
        return repository.findAll()
    }

    fun findById(id: Long): Person {
        return repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }!!
    }

    fun update(person: Person): Person {
        val entity: Person = repository.findById(person.id)
            .orElseThrow(Supplier { ResourceNotFoundException("No records found for this ID") })
        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender
        return repository.save(entity)
    }

    fun delete(id: Long) {
        val entity: Person = repository.findById(id)
            .orElseThrow(Supplier { ResourceNotFoundException("No records found for this ID") })
        repository.delete(entity)
    }
}