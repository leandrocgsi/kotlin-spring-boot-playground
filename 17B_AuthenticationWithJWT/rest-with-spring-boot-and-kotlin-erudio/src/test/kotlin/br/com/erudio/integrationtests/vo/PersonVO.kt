package br.com.erudio.integrationtests.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
@JsonIgnoreProperties
@JsonPropertyOrder( "id", "firstName", "lastName", "address", "gender" )
data class PersonVO (

    var id: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var address: String = "",
    var gender: String = ""
)