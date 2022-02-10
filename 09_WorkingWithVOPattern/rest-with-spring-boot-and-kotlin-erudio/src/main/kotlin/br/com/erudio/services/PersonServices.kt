package br.com.erudio.services

import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.exception.ResourceNotFoundException
import br.com.erudio.mapper.DozerConverter
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.function.Supplier

@Service
class PersonServices {

    @Autowired
    private lateinit  var repository: PersonRepository

    fun create(person: PersonVO?): PersonVO? {
        val entity: Person = DozerConverter.parseObject(person, Person::class.java)
        return DozerConverter.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun findAll(): List<PersonVO>? {
        return DozerConverter.parseListObjects(repository.findAll(), PersonVO::class.java)
    }

    fun findById(id: Long?): PersonVO? {
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        return DozerConverter.parseObject(entity, PersonVO::class.java)
    }

    fun update(person: PersonVO?): PersonVO? {
        val entity = repository.findById(person!!.id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        entity.firstName = person.firstName!!
        entity.lastName = person.lastName!!
        entity.address = person.address!!
        entity.gender = person.gender!!
        return DozerConverter.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun delete(id: Long?) {
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        repository.delete(entity)
    }
}