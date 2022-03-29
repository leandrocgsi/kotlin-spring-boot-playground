package br.com.erudio.services

import br.com.erudio.controller.PersonController
import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.exceptions.RequiredObjectIsNullException
import br.com.erudio.exceptions.ResourceNotFoundException
import br.com.erudio.mapper.DozerMapper
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.logging.Logger

// https://stackoverflow.com/questions/63439423/how-to-extend-collectionmodel-pagedmodel-in-spring-hateoas
// https://stackoverflow.com/questions/63087074/pagingandsortingrepository-custom-pageable-response-structure/63087937#63087937

@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository

    @Autowired
    private lateinit var pagedResourcesAssembler: PagedResourcesAssembler<PersonVO>

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAllZ1(pageable: Pageable): Page<PersonVO> {
        logger.info("Finding all people!")
        val page = repository.findAll(pageable)
        var vos = page.map { p -> DozerMapper.parseObject(p, PersonVO::class.java) }
        vos.map { p -> p.add(linkTo(PersonController::class.java).slash(p.key).withSelfRel()) }
        val findAllLink = linkTo(
            WebMvcLinkBuilder.methodOn(PersonController::class.java)
                .findAll(pageable.pageNumber, pageable.pageSize, "asc")
        ).withSelfRel()
        //val mypage: Page<PersonVO> = PageImpl(vos.content, pageable, vos.totalElements /*, linkTo(findAllLink)*/)
        return vos
    }

    fun findAllZ2(pageable: Pageable): CollectionModel<PersonVO> {
        logger.info("Finding all people!")
        val page = repository.findAll(pageable)
        var vos = page.map { p -> DozerMapper.parseObject(p, PersonVO::class.java) }
        vos.map { p -> p.add(linkTo(PersonController::class.java).slash(p.key).withSelfRel()) }
        val findAllLink = linkTo(
            WebMvcLinkBuilder.methodOn(PersonController::class.java)
                .findAll(pageable.pageNumber, pageable.pageSize, "asc")
        ).withSelfRel()
        //val mypage: Page<PersonVO> = PageImpl(vos.content, pageable, vos.totalElements /*, linkTo(findAllLink)*/)
        return CollectionModel.of<PersonVO>(vos, findAllLink)
    }

    // I OWE A BEER -> https://niks36.medium.com/spring-hateoas-part-2-a06a57fbee02
    fun findAll1(pageable: Pageable): Page<PersonVO> {
        logger.info("Finding all people!")
        val page = repository.findAll(pageable)
        var vos = page.map { p -> DozerMapper.parseObject(p, PersonVO::class.java) }
        vos.map { p -> p.add(linkTo(PersonController::class.java).slash(p.key).withSelfRel()) }
        return vos
    }

    // I OWE A BEER -> https://niks36.medium.com/spring-hateoas-part-2-a06a57fbee02
    fun findAll(pageable: Pageable): PagedModel<EntityModel<PersonVO>> {
        logger.info("Finding all people!")
        val page = repository.findAll(pageable)
        var vos = page.map { p -> DozerMapper.parseObject(p, PersonVO::class.java) }
        vos.map { p -> p.add(linkTo(PersonController::class.java).slash(p.key).withSelfRel()) }
        val findAllLink = linkTo(
            WebMvcLinkBuilder.methodOn(PersonController::class.java)
                .findAll(pageable.pageNumber, pageable.pageSize, "asc")
        ).withSelfRel()
        return pagedResourcesAssembler.toModel(vos, findAllLink)
    }

    fun findById(id: Long): PersonVO {
        logger.info("Finding one person with ID $id!")
        var person = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!") }
        val personVO: PersonVO = DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun create(person: PersonVO?) : PersonVO{
        if (person == null) throw RequiredObjectIsNullException()
        logger.info("Creating one person with name ${person.firstName}!")
        var entity: Person = DozerMapper.parseObject(person, Person::class.java)
        val personVO: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun update(person: PersonVO?) : PersonVO{
        if (person == null) throw RequiredObjectIsNullException()
        logger.info("Updating one person with ID ${person.key}!")
        val entity = repository.findById(person.key)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!") }

        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender
        val personVO: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    @Transactional
    fun disablePerson(id: Long?): PersonVO? {
        repository.disablePersons(id)
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        val personVO: PersonVO? = DozerMapper.parseObject(entity, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO!!.key).withSelfRel()
        personVO!!.add(withSelfRel)
        return personVO
    }

    fun delete(id: Long) {
        logger.info("Deleting one person with ID $id!")
        val entity = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!") }
        repository.delete(entity)
    }
}