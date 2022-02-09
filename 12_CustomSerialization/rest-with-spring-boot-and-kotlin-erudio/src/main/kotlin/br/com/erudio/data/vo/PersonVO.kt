package br.com.erudio.data.vo

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder("id", "address", "first_name", "last_name", "gender")
class PersonVO : Serializable {

    var id: Long? = null

    @JsonProperty("first_name")
    var firstName: String? = null

    @JsonProperty("last_name")
    var lastName: String? = null
    
    var address: String? = null

    @JsonIgnore
    var gender: String? = null
}