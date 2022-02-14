package br.com.erudio.integrationtests.vo

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import jakarta.xml.bind.annotation.XmlRootElement
import org.springframework.hateoas.RepresentationModel
import java.io.Serializable
import java.util.*

@XmlRootElement
@JsonPropertyOrder("id", "author", "launchDate", "price", "title")
class BookVO : RepresentationModel<BookVO?>(), Serializable {
    var id: Long? = null
    var author: String? = null
    var launchDate: Date? = null
    var price: Double? = null
    var title: String? = null
}