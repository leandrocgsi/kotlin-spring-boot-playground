package br.com.erudio.controller

import br.com.erudio.data.vo.v1.BookVO
import br.com.erudio.services.BookServices
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.function.Consumer

@Tag(name = "Book Endpoint")
@RestController
@RequestMapping("/api/book/v1")
class BookController {
    @Autowired
    private val service: BookServices? = null
    @Operation(summary = "Find all books")
    @GetMapping
    fun findAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "limit", defaultValue = "12") limit: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String?
    ): ResponseEntity<CollectionModel<BookVO>> {
        val sortDirection = if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "title"))
        val books: Page<BookVO> = service.findAll(pageable)
        books
            .stream()
            .forEach(
                Consumer<BookVO> { p: BookVO ->
                    p.add(
                        linkTo(methodOn(BookController::class.java).findById(p.getKey())).withSelfRel()
                    )
                }
            )
        return ResponseEntity.ok(CollectionModel.of(books))
    }

    @Operation(summary = "Find a specific book by your ID")
    @GetMapping(value = ["/{id}"])
    fun findById(@PathVariable("id") id: Long?): BookVO {
        val bookVO: BookVO = service.findById(id)
        bookVO.add(linkTo(methodOn(BookController::class.java).findById(id)).withSelfRel())
        return bookVO
    }

    @Operation(summary = "Create a new book")
    @PostMapping
    fun create(@RequestBody book: BookVO?): BookVO {
        val bookVO: BookVO = service.create(book)
        bookVO.add(linkTo(methodOn(BookController::class.java).findById(bookVO.getKey())).withSelfRel())
        return bookVO
    }

    @Operation(summary = "Update a specific book")
    @PutMapping
    fun update(@RequestBody book: BookVO?): BookVO {
        val bookVO: BookVO = service.update(book)
        bookVO.add(linkTo(methodOn(BookController::class.java).findById(bookVO.getKey())).withSelfRel())
        return bookVO
    }

    @Operation(summary = "Delete a specific book by your ID")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long?): ResponseEntity<*> {
        service.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }
}