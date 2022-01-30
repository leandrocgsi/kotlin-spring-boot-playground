package br.com.erudio.controller

import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.services.PersonServices
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.function.Consumer

@Tag(name = "Person Endpoint")
@RestController
@RequestMapping("/api/person/v1")
class PersonController {
    @Autowired
    private val service: PersonServices? = null
    @Operation(summary = "Find all people")
    @GetMapping
    fun findAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "limit", defaultValue = "12") limit: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String?
    ): ResponseEntity<CollectionModel<PersonVO>> {
        val sortDirection = if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"))
        val persons: Page<PersonVO> = service.findAll(pageable)
        persons
            .stream()
            .forEach(
                Consumer<PersonVO> { p: PersonVO ->
                    p.add(
                        linkTo(methodOn(PersonController::class.java).findById(p.getKey())).withSelfRel()
                    )
                }
            )
        val findAllLink: Link =
            linkTo(methodOn(PersonController::class.java).findAll(page, limit, direction)).withSelfRel()
        return ResponseEntity.ok(CollectionModel.of(persons, findAllLink))
    }

    @Operation(summary = "Find a specific person by name")
    @GetMapping(value = ["/findPersonByName/{firstName}"])
    fun findPersonByName(
        @PathVariable("firstName") firstName: String?,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "limit", defaultValue = "12") limit: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String?
    ): ResponseEntity<CollectionModel<PersonVO>> {
        val sortDirection = if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"))
        val persons: Page<PersonVO> = service.findPersonByName(firstName, pageable)
        persons
            .stream()
            .forEach(
                Consumer<PersonVO> { p: PersonVO ->
                    p.add(
                        linkTo(methodOn(PersonController::class.java).findById(p.getKey())).withSelfRel()
                    )
                }
            )
        return ResponseEntity.ok(CollectionModel.of(persons))
    }

    @Operation(summary = "Find a specific person by your ID")
    @GetMapping(value = ["/{id}"])
    fun findById(@PathVariable("id") id: Long?): PersonVO {
        val personVO: PersonVO = service.findById(id)
        personVO.add(linkTo(methodOn(PersonController::class.java).findById(id)).withSelfRel())
        return personVO
    }

    @Operation(summary = "Create a new person")
    @PostMapping
    fun create(@RequestBody person: PersonVO?): PersonVO {
        val personVO: PersonVO = service.create(person)
        personVO.add(linkTo(methodOn(PersonController::class.java).findById(personVO.getKey())).withSelfRel())
        return personVO
    }

    @Operation(summary = "Update a specific person")
    @PutMapping
    fun update(@RequestBody person: PersonVO?): PersonVO {
        val personVO: PersonVO = service.update(person)
        personVO.add(linkTo(methodOn(PersonController::class.java).findById(personVO.getKey())).withSelfRel())
        return personVO
    }

    @Operation(summary = "Disable a specific person by your ID")
    @PatchMapping(value = ["/{id}"])
    fun disablePerson(@PathVariable("id") id: Long?): PersonVO {
        val personVO: PersonVO = service.disablePerson(id)
        personVO.add(linkTo(methodOn(PersonController::class.java).findById(id)).withSelfRel())
        return personVO
    }

    @Operation(summary = "Delete a specific person by your ID")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long?): ResponseEntity<*> {
        service.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }
}