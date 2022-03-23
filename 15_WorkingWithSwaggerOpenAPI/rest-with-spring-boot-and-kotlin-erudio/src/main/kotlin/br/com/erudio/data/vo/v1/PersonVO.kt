package br.com.erudio.data.vo.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel

@JsonPropertyOrder( "id", "firstName", "lastName", "address", "gender" )
data class PersonVO (

    @Mapping("id")
    @JsonProperty("id")
    var key: Long? = null,
    var firstName: String = "",
    var lastName: String = "",
    var address: String = "",
    var gender: String = ""
) : RepresentationModel<PersonVO>()