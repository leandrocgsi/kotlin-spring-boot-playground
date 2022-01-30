package br.com.erudio.data.vo.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel
import java.io.Serializable

@JsonPropertyOrder("id", "firstName", "lastName", "address", "gender", "enabled")
class PersonVO : RepresentationModel<PersonVO?>(), Serializable {

	@Mapping("id")
	@JsonProperty("id")
	var key: Long? = null
	var firstName: String? = null
	var lastName: String? = null
	var address: String? = null
	var gender: String? = null
	var enabled: Boolean? = null

	companion object {
		private const val serialVersionUID = 1L
	}
}