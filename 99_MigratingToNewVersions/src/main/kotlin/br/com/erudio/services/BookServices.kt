package br.com.erudio.services

import br.com.erudio.converter.DozerConverter
import br.com.erudio.data.model.Book
import br.com.erudio.data.vo.v1.BookVO
import br.com.erudio.exception.ResourceNotFoundException
import br.com.erudio.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.function.Supplier

@Service
class BookServices {
	@Autowired
	var repository: BookRepository? = null
	fun create(book: BookVO?): BookVO {
		val entity: Book =
			DozerConverter.parseObject<BookVO, Book>(
				book,
				Book::class.java
			)
		return DozerConverter.parseObject<Book, BookVO>(
			repository!!.save(entity),
			BookVO::class.java
		)
	}

	fun findAll(pageable: Pageable?): Page<BookVO> {
		val page = repository!!.findAll(pageable!!)
		return page.map { entity: Book? ->
			convertToBookVO(
				entity
			)
		}
	}

	private fun convertToBookVO(entity: Book?): BookVO {
		return DozerConverter.parseObject<Book, BookVO>(entity, BookVO::class.java)
	}

	fun findById(id: Long): BookVO {
		val entity = repository!!.findById(id)
			.orElseThrow {
				ResourceNotFoundException(
					"No records found for this ID"
				)
			}!!
		return DozerConverter.parseObject<Book, BookVO>(entity, BookVO::class.java)
	}

	fun update(book: BookVO): BookVO {
		val entity: Book = repository.findById(book.key)
			.orElseThrow(Supplier {
				ResourceNotFoundException(
					"No records found for this ID"
				)
			})
		entity.author = book.author
		entity.launchDate = book.launchDate
		entity.price = book.price
		entity.title = book.title
		return DozerConverter.parseObject<Book, BookVO>(
			repository.save(entity),
			BookVO::class.java
		)
	}

	fun delete(id: Long) {
		val entity: Book = repository.findById(id)
			.orElseThrow(Supplier {
				ResourceNotFoundException(
					"No records found for this ID"
				)
			})
		repository.delete(entity)
	}
}