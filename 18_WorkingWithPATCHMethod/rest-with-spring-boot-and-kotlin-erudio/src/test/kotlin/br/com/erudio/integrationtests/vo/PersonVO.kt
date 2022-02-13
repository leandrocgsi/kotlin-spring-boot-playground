package br.com.erudio.integrationtests.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import jakarta.xml.bind.annotation.XmlRootElement
import org.springframework.hateoas.RepresentationModel

@XmlRootElement
@JsonIgnoreProperties
@JsonPropertyOrder( "id", "firstName", "lastName", "address", "gender" )
class PersonVO : RepresentationModel<PersonVO>() {

    var id: Long? = null
    var firstName: String? = null
    var lastName: String? = null
    var address: String? = null
    var gender: String? = null
}