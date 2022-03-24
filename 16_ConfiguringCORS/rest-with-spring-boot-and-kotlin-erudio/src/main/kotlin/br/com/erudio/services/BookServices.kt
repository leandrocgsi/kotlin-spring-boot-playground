package br.com.erudio.services

import br.com.erudio.controller.BookController
import br.com.erudio.data.vo.v1.BookVO
import br.com.erudio.exception.RequiredObjectIsNullException
import br.com.erudio.exception.ResourceNotFoundException
import br.com.erudio.mapper.DozerMapper
import br.com.erudio.model.Book
import br.com.erudio.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class BookServices {

    private val logger = Logger.getLogger(BookServices::class.java.name)

    @Autowired
    private lateinit  var repository: BookRepository

    fun findAll(): List<BookVO> {
        logger.info("Finding all people!")
        val books = DozerMapper.parseListObjects(repository.findAll(), BookVO::class.java)
        for (value in books) {
            val withSelfRel = linkTo(BookController::class.java).slash(value.key).withSelfRel()
            value.add(withSelfRel)
        }
        return books
    }

    fun findById(id: Long): BookVO {

        logger.info("Finding one book with ID $id!")

        val entity = repository.findById(id)
            .orElseThrow<RuntimeException> { ResourceNotFoundException("No records found for this ID") }
        val bookVO: BookVO = DozerMapper.parseObject(entity, BookVO::class.java)

        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)

        return bookVO
    }

    fun create(book: BookVO?): BookVO {
        if (book == null) throw RequiredObjectIsNullException()

        logger.info("Creating one book with title ${book?.title}!")

        val entity: Book = DozerMapper.parseObject(book, Book::class.java)
        val persisted = repository.save(entity)
        val bookVO: BookVO = DozerMapper.parseObject(persisted, BookVO::class.java)

        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)

        return bookVO
    }

    fun update(book: BookVO?): BookVO {
    if (book == null) throw RequiredObjectIsNullException()

        logger.info("Updating a book with title ${book.title}!")

        val entity: Book = repository.findById(book.key!!)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }
        entity.author = book.author
        entity.title = book.title
        entity.price = book.price
        //entity.launchDate = book.launchDate
        val bookVO: BookVO = DozerMapper.parseObject(repository.save(entity), BookVO::class.java)

        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)

        return bookVO
    }

    fun delete(id: Long)  {

        logger.info("Deleting one book with ID $id!")
        val entity: Book = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }
        repository.delete(entity)
    }
}