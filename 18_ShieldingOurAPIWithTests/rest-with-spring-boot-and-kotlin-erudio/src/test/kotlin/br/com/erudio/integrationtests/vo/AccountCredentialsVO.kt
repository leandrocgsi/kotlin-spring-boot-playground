package br.com.erudio.integrationtests.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.xml.bind.annotation.XmlRootElement

//@XmlRootElement
//@JsonIgnoreProperties
data class AccountCredentialsVO (
    var username: String? = null,
    var password: String? = null
)