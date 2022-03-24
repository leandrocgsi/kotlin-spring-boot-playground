package br.com.erudio.data.vo.v1

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel
import java.util.Date

@JsonPropertyOrder("id", "author", "launchDate", "price", "title" )
class BookVO (

    @Mapping("id")
    @field:JsonProperty("id")
    var key: Long? = null,
    var author: String = "",

    //@field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    var launchDate: Date? = null,
    var price: Double = 0.0,
    var title: String = ""
) : RepresentationModel<BookVO>()