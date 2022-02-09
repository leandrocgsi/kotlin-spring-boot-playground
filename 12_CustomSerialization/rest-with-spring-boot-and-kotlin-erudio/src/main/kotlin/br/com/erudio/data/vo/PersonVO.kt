package br.com.erudio.data.vo

// https://www.baeldung.com/kotlin/jackson-kotlin
// https://www.http4k.org/guide/howto/create_a_custom_json_marshaller/
// https://proandroiddev.com/parsing-optional-values-with-jackson-and-kotlin-36f6f63868ef
// https://hceris.com/painless-json-with-kotlin-and-jackson/

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.io.Serializable

//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder("id", "address", "first_name", "last_name", "gender")
data class PersonVO (

    var id: Long = 0,

    @field:JsonProperty("first_name")
    var firstName: String = "",

    @field:JsonProperty("last_name")
    var lastName: String = "",

    var address: String = "",

    @field:JsonIgnore
    var gender: String = ""
)