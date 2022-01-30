package br.com.erudio.data.vo.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel
import java.io.Serializable
import java.util.*

@JsonPropertyOrder("id", "author", "launchDate", "price", "title")
class BookVO : RepresentationModel<BookVO?>(), Serializable {
	@Mapping("id")
	@JsonProperty("id")
	var key: Long? = null
	var author: String? = null
	var launchDate: Date? = null
	var price: Double? = null
	var title: String? = null

	companion object {
		private const val serialVersionUID = 1L
	}
}