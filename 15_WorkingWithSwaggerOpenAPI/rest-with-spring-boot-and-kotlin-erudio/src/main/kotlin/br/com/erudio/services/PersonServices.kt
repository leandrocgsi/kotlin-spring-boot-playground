package br.com.erudio.services

import br.com.erudio.controller.PersonController
import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.exception.RequiredObjectIsNullException
import br.com.erudio.exception.ResourceNotFoundException
import br.com.erudio.mapper.DozerMapper
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class PersonServices {

    private val logger = Logger.getLogger(PersonServices::class.java.name)

    @Autowired
    private lateinit  var repository: PersonRepository

    fun findAll(): List<PersonVO> {
        logger.info("Finding all people!")
        val persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO::class.java)
        for (value in persons) {
            val withSelfRel = linkTo(PersonController::class.java).slash(value.key).withSelfRel()
            value.add(withSelfRel)
        }
        return persons
    }

    fun findById(id: Long): PersonVO {

        logger.info("Finding one person with ID $id!")

        val entity = repository.findById(id)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        val personVO: PersonVO = DozerMapper.parseObject(entity, PersonVO::class.java)

        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)

        return personVO
    }

    fun create(person: PersonVO?): PersonVO {
        if (person == null) throw RequiredObjectIsNullException()

        logger.info("Creating one person with name ${person?.firstName}!")

        val entity: Person = DozerMapper.parseObject(person, Person::class.java)
        val personVO: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)

        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)

        return personVO
    }

    fun update(person: PersonVO?): PersonVO {
    if (person == null) throw RequiredObjectIsNullException()

        logger.info("Updating a person with name ${person!!.firstName}!")

        val entity: Person = repository.findById(person.key!!)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }
        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender
        val personVO: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)

        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)

        return personVO
    }

    fun delete(id: Long)  {

        logger.info("Deleting one person with ID $id!")
        val entity: Person = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }
        repository.delete(entity)
    }
}