package br.com.erudio.data.vo.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel

@JsonPropertyOrder( "id", "firstName", "lastName", "Addresss", "gender" )
class PersonVO : RepresentationModel<PersonVO>() {

    @Mapping("id")
    @JsonProperty("id")
    var key: Long? = null
    var firstName: String? = null
    var lastName: String? = null
    var address: String? = null
    var gender: String? = null
    var enabled: Boolean = true
}