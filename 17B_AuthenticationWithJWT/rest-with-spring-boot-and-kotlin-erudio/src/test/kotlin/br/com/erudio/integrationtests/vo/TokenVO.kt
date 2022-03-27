package br.com.erudio.integrationtests.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.xml.bind.annotation.XmlRootElement
import java.util.*

@XmlRootElement
@JsonIgnoreProperties
data class TokenVO (
    var username: String? = null,
    var authenticated: Boolean? = null,
    var created: Date? = null,
    var expiration: Date? = null,
    var accessToken: String? = null,
    var refreshToken: String? = null,
)