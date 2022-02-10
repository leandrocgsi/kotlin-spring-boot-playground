package br.com.erudio.services

import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.data.vo.v2.PersonVO as PersonVOV2
import br.com.erudio.exception.ResourceNotFoundException
import br.com.erudio.mapper.DozerConverter
import br.com.erudio.mapper.custom.PersonConverter
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PersonServices {

    @Autowired
    private lateinit  var repository: PersonRepository

    @Autowired
    private lateinit  var converter: PersonConverter

    fun create(person: PersonVO?): PersonVO? {
        val entity: Person = DozerConverter.parseObject(person, Person::class.java)
        return DozerConverter.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun createV2(person: PersonVOV2?): PersonVOV2? {
        val entity = converter.convertVOToEntity(person!!)
        return converter.convertEntityToVO(repository.save(entity))
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