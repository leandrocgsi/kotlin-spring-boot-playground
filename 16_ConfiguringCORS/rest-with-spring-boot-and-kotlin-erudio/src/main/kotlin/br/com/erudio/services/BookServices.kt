package br.com.erudio.services

import br.com.erudio.controller.BookController
import br.com.erudio.data.vo.v1.BookVO
import br.com.erudio.exception.RequiredObjectIsNullException
import br.com.erudio.exception.ResourceNotFoundException
import br.com.erudio.mapper.DozerConverter
import br.com.erudio.model.Book
import br.com.erudio.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Service


@Service
class BookServices {

    // https://github.com/olszewskimichal/Hateoas-SpringBoot-Kotlin/blob/master/src/main/kotlin/com/example/hateoas/kotlin/DemoApplication.kt
    @Autowired
    private lateinit var repository: BookRepository

    fun findAll(): List<BookVO>? {
        val books = DozerConverter.parseListObjects(repository.findAll(), BookVO::class.java)
        for (value in books!!) {
            val withSelfRel = WebMvcLinkBuilder.linkTo(BookController::class.java).slash(value!!.key).withSelfRel()
            value!!.add(withSelfRel)
        }
        return books
    }

    fun findById(id: Long?): BookVO? {
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        val bookVO: BookVO? = DozerConverter.parseObject(entity, BookVO::class.java)
        val withSelfRel = WebMvcLinkBuilder.linkTo(BookController::class.java).slash(bookVO!!.key).withSelfRel()
        bookVO!!.add(withSelfRel)
        return bookVO
    }

    fun create(book: BookVO?): BookVO? {
        if (book == null) throw RequiredObjectIsNullException()
        val entity: Book = DozerConverter.parseObject(book, Book::class.java)
        var bookVO: BookVO? = DozerConverter.parseObject(repository.save(entity), BookVO::class.java)
        val withSelfRel = WebMvcLinkBuilder.linkTo(BookController::class.java).slash(bookVO!!.key).withSelfRel()
        bookVO!!.add(withSelfRel)
        return bookVO
    }

    fun update(book: BookVO?): BookVO? {
        if (book == null) throw RequiredObjectIsNullException()
        val entity = repository.findById(book!!.key!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        entity.author = book.author!!
        entity.title = book.title!!
        entity.price = book.price!!
        entity.launchDate = book.launchDate!!
        var bookVO: BookVO? = DozerConverter.parseObject(repository.save(entity), BookVO::class.java)
        val withSelfRel = WebMvcLinkBuilder.linkTo(BookController::class.java).slash(bookVO!!.key).withSelfRel()
        bookVO!!.add(withSelfRel)
        return bookVO
    }

    fun delete(id: Long?) {
        val entity = repository.findById(id!!)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        repository.delete(entity)
    }
}