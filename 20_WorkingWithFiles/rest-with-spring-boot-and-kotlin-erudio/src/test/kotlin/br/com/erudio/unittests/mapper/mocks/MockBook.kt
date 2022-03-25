package br.com.erudio.unittests.mapper.mocks

import br.com.erudio.data.vo.v1.BookVO
import br.com.erudio.model.Book
import java.util.*

class MockBook {
    fun mockEntityList(): ArrayList<Book> {
        val books: ArrayList<Book> = ArrayList<Book>()
        for (i in 0..13) {
            books.add(mockEntity(i))
        }
        return books
    }

    fun mockVOList(): ArrayList<BookVO> {
        val books: ArrayList<BookVO> = ArrayList()
        for (i in 0..13) {
            books.add(mockVO(i))
        }
        return books
    }

    fun mockEntity(number: Int): Book {
        val book = Book()
        book.id = number.toLong()
        book.author = "Some Author$number"
        book.launchDate = Date()
        book.price = 25.0
        book.title = "Some Title$number"
        return book
    }

    fun mockVO(number: Int): BookVO {
        val book = BookVO()
        book.key = number.toLong()
        book.author = "Some Author$number"
        book.launchDate = Date()
        book.price = 25.0
        book.title = "Some Title$number"
        return book
    }
}