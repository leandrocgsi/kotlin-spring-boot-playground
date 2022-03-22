package br.com.erudio.services

import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.data.vo.v2.PersonVO as PersonVOV2
import br.com.erudio.exception.ResourceNotFoundException
import br.com.erudio.mapper.DozerMapper
import br.com.erudio.mapper.custom.PersonMapper
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class PersonServices {

    private val logger = Logger.getLogger(PersonServices::class.java.name)

    @Autowired
    private lateinit  var repository: PersonRepository

    @Autowired
    private lateinit  var mapper: PersonMapper

    fun findAll(): List<PersonVO> {
        logger.info("Finding all people!")
        return DozerMapper.parseListObjects(repository.findAll(), PersonVO::class.java)
    }

    fun findById(id: Long): PersonVO {

        logger.info("Finding one person with ID $id!")

        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        return DozerMapper.parseObject(entity, PersonVO::class.java)
    }

    fun create(person: PersonVO): PersonVO {
        logger.info("Creating one person with name ${person.firstName}!")

        val entity: Person = DozerMapper.parseObject(person, Person::class.java)
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun createV2(person: PersonVOV2): PersonVOV2 {
        val entity = mapper.mapVOToEntity(person!!)
        return mapper.mapEntityToVO(repository.save(entity))
    }

    fun update(person: PersonVO): PersonVO {

        logger.info("Updating a person with name ${person.firstName}!")

        val entity: Person = repository.findById(person.id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }
        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun delete(id: Long)  {

        logger.info("Deleting one person with ID $id!")
        val entity: Person = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }
        repository.delete(entity)
    }
}