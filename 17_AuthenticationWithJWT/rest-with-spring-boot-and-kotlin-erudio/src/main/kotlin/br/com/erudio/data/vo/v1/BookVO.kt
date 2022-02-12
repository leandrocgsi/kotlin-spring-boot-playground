package br.com.erudio.data.vo.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import jakarta.persistence.*
import org.springframework.hateoas.RepresentationModel
import java.util.*

@JsonPropertyOrder("id", "author", "launchDate", "price", "title" )
class BookVO : RepresentationModel<BookVO>() {

    @Mapping("id")
    @JsonProperty("id")
    var key: Long? = null
    var author: String = ""
    var launchDate: Date = Date()
    var price: Double = 0.toDouble()
    var title: String = ""
}