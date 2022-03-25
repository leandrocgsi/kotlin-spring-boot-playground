package br.com.erudio.services

import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.exception.ResourceNotFoundException
import br.com.erudio.mapper.DozerMapper
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PersonServices {

    @Autowired
    private lateinit  var repository: PersonRepository

    fun create(person: PersonVO?): PersonVO? {
        val entity: Person = DozerMapper.parseObject(person, Person::class.java)
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun findAll(): List<PersonVO>? {
        return DozerMapper.parseListObjects(repository.findAll(), PersonVO::class.java)
    }

    fun findById(id: Long?): PersonVO? {
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        return DozerMapper.parseObject(entity, PersonVO::class.java)
    }

    fun update(person: PersonVO?): PersonVO? {
        val entity = repository.findById(person!!.id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        entity.firstName = person.firstName!!
        entity.lastName = person.lastName!!
        entity.address = person.address!!
        entity.gender = person.gender!!
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun delete(id: Long?) {
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        repository.delete(entity)
    }
}