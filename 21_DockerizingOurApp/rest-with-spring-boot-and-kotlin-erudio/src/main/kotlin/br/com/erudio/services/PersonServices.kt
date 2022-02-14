package br.com.erudio.services

import br.com.erudio.controller.PersonController
import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.exception.RequiredObjectIsNullException
import br.com.erudio.exception.ResourceNotFoundException
import br.com.erudio.mapper.DozerConverter
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class PersonServices {
    // https://github.com/olszewskimichal/Hateoas-SpringBoot-Kotlin/blob/master/src/main/kotlin/com/example/hateoas/kotlin/DemoApplication.kt
    @Autowired
    private lateinit  var repository: PersonRepository

    fun findAll(pageable: Pageable): CollectionModel<PersonVO?>? {
        val page = repository.findAll(pageable)
        val persons: Page<PersonVO> = DozerConverter.parsePageOfObjects(page, PersonVO::class.java)

        for (person in persons.content    ) {
            val withSelfRel = linkTo(PersonController::class.java).slash(person!!.key).withSelfRel()
            person.add(withSelfRel)
        }

        val findAllLink = linkTo(
            methodOn(PersonController::class.java)
                .findAll(pageable.pageNumber, pageable.pageSize, "asc")!!
        ).withSelfRel()

        return CollectionModel.of<PersonVO>(persons, findAllLink)
    }

    fun findPersonByName(firstName: String?, pageable: Pageable): CollectionModel<PersonVO?>? {
        val page: Page<Person?>? = repository.findPersonByName(firstName, pageable)

        val persons: Page<PersonVO> = DozerConverter.parsePageOfObjects(page, PersonVO::class.java)

        for (person in persons.content    ) {
            val withSelfRel = linkTo(PersonController::class.java).slash(person!!.key).withSelfRel()
            person.add(withSelfRel)
        }

        val findAllLink = linkTo(
                methodOn(PersonController::class.java)
                    .findPersonByName(firstName, pageable.pageNumber, pageable.pageSize, "asc")!!
            ).withSelfRel()

        return CollectionModel.of<PersonVO>(persons, findAllLink)
    }

    fun findById(id: Long?): PersonVO? {
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        val personVO: PersonVO = DozerConverter.parseObject(entity, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun create(person: PersonVO?): PersonVO? {
        if (person == null) throw RequiredObjectIsNullException()
        val entity: Person = DozerConverter.parseObject(person, Person::class.java)
        val personVO: PersonVO = DozerConverter.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun update(person: PersonVO?): PersonVO? {
        if (person == null) throw RequiredObjectIsNullException()
        val entity = repository.findById(person.key!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        entity.firstName = person.firstName!!
        entity.lastName = person.lastName!!
        entity.address = person.address!!
        entity.gender = person.gender!!
        val personVO: PersonVO = DozerConverter.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    @Transactional
    fun disablePerson(id: Long?): PersonVO? {
        repository.disablePersons(id)
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        val personVO: PersonVO = DozerConverter.parseObject(entity, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }
    fun delete(id: Long?) {
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        repository.delete(entity)
    }
}
