package br.com.erudio.services

import br.com.erudio.controller.PersonController
import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.exception.RequiredObjectIsNullException
import br.com.erudio.exception.ResourceNotFoundException
import br.com.erudio.mapper.DozerConverter
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Service


@Service
class PersonServices {

    // https://github.com/olszewskimichal/Hateoas-SpringBoot-Kotlin/blob/master/src/main/kotlin/com/example/hateoas/kotlin/DemoApplication.kt
    @Autowired
    private lateinit  var repository: PersonRepository

    fun findAll(): List<PersonVO>? {
        val persons = DozerConverter.parseListObjects(repository.findAll(), PersonVO::class.java)
        for (value in persons!!) {
            val withSelfRel = WebMvcLinkBuilder.linkTo(PersonController::class.java).slash(value!!.key).withSelfRel()
            value!!.add(withSelfRel)
        }
        return persons
    }

    fun findById(id: Long?): PersonVO? {
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        val personVO: PersonVO? = DozerConverter.parseObject(entity, PersonVO::class.java)
        val withSelfRel = WebMvcLinkBuilder.linkTo(PersonController::class.java).slash(personVO!!.key).withSelfRel()
        personVO!!.add(withSelfRel)
        return personVO
    }

    fun create(person: PersonVO?): PersonVO? {
        if (person == null) throw RequiredObjectIsNullException()
        val entity: Person = DozerConverter.parseObject(person, Person::class.java)
        var personVO: PersonVO? = DozerConverter.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = WebMvcLinkBuilder.linkTo(PersonController::class.java).slash(personVO!!.key).withSelfRel()
        personVO!!.add(withSelfRel)
        return personVO
    }

    fun update(person: PersonVO?): PersonVO? {
        if (person == null) throw RequiredObjectIsNullException()
        val entity = repository.findById(person!!.key!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        entity.firstName = person.firstName!!
        entity.lastName = person.lastName!!
        entity.address = person.address!!
        entity.gender = person.gender!!
        var personVO: PersonVO? = DozerConverter.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = WebMvcLinkBuilder.linkTo(PersonController::class.java).slash(personVO!!.key).withSelfRel()
        personVO!!.add(withSelfRel)
        return personVO
    }

    fun delete(id: Long?) {
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        repository.delete(entity)
    }
}