package br.com.erudio.util

import org.assertj.core.util.diff.Chunk
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.util.function.Function
import java.util.List


class PageImpl<T> : Chunk<T>, Page<T> {

    @JvmOverloads
    constructor(
        content: List<T>?, pageable: Pageable = Pageable.unpaged(), total: Long = content?.size?.toLong()
            ?: 0
    ) : super(content!!.size, content.toList()) {
        this.total = pageable.toOptional().filter { it -> !content.isEmpty() }
            .filter { it -> it.offset + it.pageSize > total }
            .map { it -> it.offset + content.size }
            .orElse(total)
    }

    private val total: Long

    override fun isLast(): Boolean {
        return !hasNext()
    }

    override fun hasNext(): Boolean {
        return number + 1 < totalPages
    }

    override fun getTotalPages(): Int {
        return if (size == 0) 1 else Math.ceil(total.toDouble() / size.toDouble()).toInt()
    }

    override fun getTotalElements(): Long {
        return total
    }

    override fun <U : Any?> map(converter: Function<in T, out U>): Page<U> {
        // return PageImpl<T>(Chunk.getConvertedContent(converter), pageable, total)

        TODO("Not yet implemented")
    }

    override fun toString(): String {
        var contentType: String? = "UNKNOWN"
        val content = content
        if (!content.isEmpty() && content[0] != null) {
            contentType = content[0]!!.javaClass.name
        }
        return String.format("Page %s of %d containing %s instances", number + 1, totalPages, contentType)
    }



    override fun getNumber(): Int {
        TODO("Not yet implemented")
    }

    override fun iterator(): MutableIterator<T> {
        TODO("Not yet implemented")
    }

    override fun getSize(): Int {
        TODO("Not yet implemented")
    }

    override fun getNumberOfElements(): Int {
        TODO("Not yet implemented")
    }

    override fun getContent(): MutableList<T> {
        TODO("Not yet implemented")
    }

    override fun hasContent(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSort(): Sort {
        TODO("Not yet implemented")
    }

    override fun isFirst(): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPrevious(): Boolean {
        TODO("Not yet implemented")
    }

    override fun nextPageable(): Pageable {
        TODO("Not yet implemented")
    }

    override fun previousPageable(): Pageable {
        TODO("Not yet implemented")
    }
}