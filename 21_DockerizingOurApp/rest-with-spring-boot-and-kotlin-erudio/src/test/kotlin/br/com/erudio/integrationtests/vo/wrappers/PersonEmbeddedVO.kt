package br.com.erudio.integrationtests.vo.wrappers

import br.com.erudio.integrationtests.vo.PersonVO
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

class PersonEmbeddedVO : Serializable {

    @JsonProperty("personVOes")
    var persons: List<PersonVO>? = null
}