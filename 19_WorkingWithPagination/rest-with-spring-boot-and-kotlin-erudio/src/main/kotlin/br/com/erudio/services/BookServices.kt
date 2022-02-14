package br.com.erudio.services

import br.com.erudio.controller.BookController
import br.com.erudio.data.vo.v1.BookVO
import br.com.erudio.exception.RequiredObjectIsNullException
import br.com.erudio.exception.ResourceNotFoundException
import br.com.erudio.mapper.DozerConverter
import br.com.erudio.model.Book
import br.com.erudio.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Service


@Service
class BookServices {

    // https://github.com/olszewskimichal/Hateoas-SpringBoot-Kotlin/blob/master/src/main/kotlin/com/example/hateoas/kotlin/DemoApplication.kt
    @Autowired
    private lateinit  var repository: BookRepository

    fun findAll(pageable: Pageable): CollectionModel<BookVO?>? {
        val page = repository.findAll(pageable)
        val books: Page<BookVO> = DozerConverter.parsePageOfObjects(page, BookVO::class.java)

        for (book in books.content    ) {
            val withSelfRel = linkTo(BookController::class.java).slash(book!!.key).withSelfRel()
            book.add(withSelfRel)
        }

        val findAllLink = linkTo(
            methodOn(BookController::class.java)
                .findAll(pageable.pageNumber, pageable.pageSize, "asc")!!
        ).withSelfRel()

        return CollectionModel.of<BookVO>(books, findAllLink)
    }

    fun findById(id: Long?): BookVO? {
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        val bookVO: BookVO = DozerConverter.parseObject(entity, BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun create(book: BookVO?): BookVO? {
        if (book == null) throw RequiredObjectIsNullException()
        val entity: Book = DozerConverter.parseObject(book, Book::class.java)
        val bookVO: BookVO? = DozerConverter.parseObject(repository.save(entity), BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO!!.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun update(book: BookVO?): BookVO? {
        if (book == null) throw RequiredObjectIsNullException()
        val entity = repository.findById(book.key!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        entity.author = book.author
        entity.title = book.title
        entity.price = book.price
        entity.launchDate = book.launchDate
        val bookVO: BookVO? = DozerConverter.parseObject(repository.save(entity), BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO!!.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun delete(id: Long?) {
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        repository.delete(entity)
    }
}