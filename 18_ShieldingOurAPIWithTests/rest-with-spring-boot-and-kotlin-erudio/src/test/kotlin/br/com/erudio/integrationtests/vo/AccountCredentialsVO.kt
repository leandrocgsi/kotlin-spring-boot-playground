package br.com.erudio.integrationtests.vo

import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
//@JsonIgnoreProperties
data class AccountCredentialsVO (
    var username: String? = null,
    var password: String? = null
)