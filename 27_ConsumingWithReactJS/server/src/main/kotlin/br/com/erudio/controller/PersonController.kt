package br.com.erudio.controller

import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.services.PersonServices
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.hateoas.CollectionModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


// https://lankydan.dev/documenting-a-spring-rest-api-following-the-openapi-specification
// https://github.com/lankydan/spring-rest-api-with-swagger

@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
class PersonController {

    @Autowired
    private lateinit  var service: PersonServices

    @GetMapping(produces = ["application/json", "application/xml", "application/x-yaml"])
    @Operation(summary = "Finds all People", description = "Finds all People.",
        tags = ["People"] ,
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(array = ArraySchema(schema = Schema(implementation = PersonVO::class)))
                ]
            ),
            ApiResponse(description = "No Content", responseCode = "204", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Bad Request", responseCode = "400", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Unauthorized", responseCode = "401", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Not Found", responseCode = "404", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Internal error", responseCode = "500", content = [Content(schema = Schema(implementation = Unit::class))])
        ]
    )
    fun findAll(@RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "limit", defaultValue = "12") limit: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String?
    ): ResponseEntity<CollectionModel<PersonVO?>?>? {
        val sortDirection: Direction = if ("desc".equals(direction, ignoreCase = true)) Direction.DESC else Direction.ASC
        val pageable: Pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"))
        return ResponseEntity.ok(service.findAll(pageable))
    }

    @GetMapping(value = ["/findPersonByName/{firstName}"], produces = ["application/json", "application/xml", "application/x-yaml"])
    @Operation(
        summary = "Find persons by name",
        description = "Find persons by name",
        tags = ["People"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = PersonVO::class))
                )]
            ),
            ApiResponse(description = "No Content", responseCode = "204", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Bad Request", responseCode = "400", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Unauthorized", responseCode = "401", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Not Found", responseCode = "404", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Internal error", responseCode = "500", content = [Content(schema = Schema(implementation = Unit::class))])
        ]
    )
    fun findPersonByName(
        @PathVariable("firstName") firstName: String?,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "limit", defaultValue = "12") limit: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String?
    ): ResponseEntity<CollectionModel<PersonVO?>?>? {
        val sortDirection: Direction = if ("desc".equals(direction, ignoreCase = true)) Direction.DESC else Direction.ASC
        val pageable: Pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"))
        return ResponseEntity.ok(service.findPersonByName(firstName, pageable))
    }

    @CrossOrigin(origins = ["http://localhost:8080"])
    @GetMapping("/{id}",
        produces = ["application/json", "application/xml", "application/x-yaml"])
    @Operation(summary = "Finds a person", description = "Find a specific person by your ID.",
        tags = ["People"] ,
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(
                        schema = Schema(implementation = PersonVO::class)
                    )
                ]
            ),
            ApiResponse(description = "No Content", responseCode = "204", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Bad Request", responseCode = "400", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Unauthorized", responseCode = "401", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Not Found", responseCode = "404", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Internal error", responseCode = "500", content = [Content(schema = Schema(implementation = Unit::class))])
        ]
    )
    fun findById(@PathVariable("id") id: Long?): PersonVO? {
        return service.findById(id!!)
    }

    @CrossOrigin(origins = ["http://localhost:8080", "https://erudio.com.br"])
    @PostMapping(
        produces = ["application/json", "application/xml", "application/x-yaml"],
        consumes = ["application/json", "application/xml", "application/x-yaml"]
    )
    @Operation(
        summary = "Adds a new person",
        description = "Adds a new person by passing in a JSON, XML or YML representation of the person.",
        tags = ["People"] ,
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(
                        schema = Schema(implementation = PersonVO::class)
                    )
                ]
            ),
            ApiResponse(description = "Bad Request", responseCode = "400", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Unauthorized", responseCode = "401", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Internal error", responseCode = "500", content = [Content(schema = Schema(implementation = Unit::class))])
        ]
    )
    fun create(@RequestBody person: PersonVO?): PersonVO? {
        return service.create(person)
    }

    @PutMapping(
        produces = ["application/json", "application/xml", "application/x-yaml"],
        consumes = ["application/json", "application/xml", "application/x-yaml"]
    )
    @Operation(
        summary = "Updates a person's information",
        description = "Updates a person's information by passing in a JSON, XML or YML representation of the updated person.",
        tags = ["People"] ,
        responses = [
            ApiResponse(
                description = "Updated",
                responseCode = "200",
                content = [
                    Content(
                        schema = Schema(implementation = PersonVO::class)
                    )
                ]
            ),
            ApiResponse(description = "No Content", responseCode = "204", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Bad Request", responseCode = "400", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Not Found", responseCode = "404", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Unauthorized", responseCode = "401", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Internal error", responseCode = "500", content = [Content(schema = Schema(implementation = Unit::class))])
        ]
    )
    fun update(@RequestBody person: PersonVO?): PersonVO? {
        return service.update(person)
    }

    @PatchMapping("/{id}",
        produces = ["application/json", "application/xml", "application/x-yaml"])
    @Operation(summary = "Disable a person", description = "Disable a specific person by your ID.",
        tags = ["People"] ,
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(
                        schema = Schema(implementation = PersonVO::class)
                    )
                ]
            ),
            ApiResponse(description = "No Content", responseCode = "204", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Bad Request", responseCode = "400", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Unauthorized", responseCode = "401", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Not Found", responseCode = "404", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Internal error", responseCode = "500", content = [Content(schema = Schema(implementation = Unit::class))])
        ]
    )
    fun disablePerson(@PathVariable("id") id: Long?): PersonVO? {
        return service.disablePerson(id!!)
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletes a person",
        description = "Deletes a person by their Id.",
        tags = ["People"] ,
        responses = [
            ApiResponse(description = "No Content", responseCode = "204", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Bad Request", responseCode = "400", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Unauthorized", responseCode = "401", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Not Found", responseCode = "404", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Internal error", responseCode = "500", content = [Content(schema = Schema(implementation = Unit::class))])
        ]
    )
    fun delete(@PathVariable("id") id: Long?): ResponseEntity<*>? {
        service.delete(id!!)
        return ResponseEntity.noContent().build<Any>()
    }
}