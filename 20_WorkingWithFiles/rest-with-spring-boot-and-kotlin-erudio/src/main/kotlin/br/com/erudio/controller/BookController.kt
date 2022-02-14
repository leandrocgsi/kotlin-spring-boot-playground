package br.com.erudio.controller

import br.com.erudio.data.vo.v1.BookVO
import br.com.erudio.services.BookServices
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
@RequestMapping("/api/book/v1")
@Tag(name = "Book", description = "Endpoints for Managing Book")
class BookController {

    @Autowired
    private lateinit  var service: BookServices

    @GetMapping(produces = ["application/json", "application/xml", "application/x-yaml"])
    @Operation(summary = "Finds all Book", description = "Finds all Book.",
        tags = ["Book"] ,
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(array = ArraySchema(schema = Schema(implementation = BookVO::class)))
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
    ): ResponseEntity<CollectionModel<BookVO?>?>? {
        val sortDirection: Direction = if ("desc".equals(direction, ignoreCase = true)) Direction.DESC else Direction.ASC
        val pageable: Pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "title"))
        return ResponseEntity.ok(service.findAll(pageable))
    }

    @GetMapping("/{id}",
        produces = ["application/json", "application/xml", "application/x-yaml"])
    @Operation(summary = "Finds a book", description = "Find a specific book by your ID.",
        tags = ["Book"] ,
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(
                        schema = Schema(implementation = BookVO::class)
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
    fun findById(@PathVariable("id") id: Long?): BookVO? {
        return service.findById(id!!)
    }

    @PostMapping(
        produces = ["application/json", "application/xml", "application/x-yaml"],
        consumes = ["application/json", "application/xml", "application/x-yaml"]
    )
    @Operation(
        summary = "Adds a new book",
        description = "Adds a new book by passing in a JSON, XML or YML representation of the book.",
        tags = ["Book"] ,
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(
                        schema = Schema(implementation = BookVO::class)
                    )
                ]
            ),
            ApiResponse(description = "Bad Request", responseCode = "400", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Unauthorized", responseCode = "401", content = [Content(schema = Schema(implementation = Unit::class))]),
            ApiResponse(description = "Internal error", responseCode = "500", content = [Content(schema = Schema(implementation = Unit::class))])
        ]
    )
    fun create(@RequestBody book: BookVO?): BookVO? {
        return service.create(book)
    }

    @PutMapping(
        produces = ["application/json", "application/xml", "application/x-yaml"],
        consumes = ["application/json", "application/xml", "application/x-yaml"]
    )
    @Operation(
        summary = "Updates a book's information",
        description = "Updates a book's information by passing in a JSON, XML or YML representation of the updated book.",
        tags = ["Book"] ,
        responses = [
            ApiResponse(
                description = "Updated",
                responseCode = "200",
                content = [
                    Content(
                        schema = Schema(implementation = BookVO::class)
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
    fun update(@RequestBody book: BookVO?): BookVO? {
        return service.update(book)
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletes a book",
        description = "Deletes a book by their Id.",
        tags = ["Book"] ,
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