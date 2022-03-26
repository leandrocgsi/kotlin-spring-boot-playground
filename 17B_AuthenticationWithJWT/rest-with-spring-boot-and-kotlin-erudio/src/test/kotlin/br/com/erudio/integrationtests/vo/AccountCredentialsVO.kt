package br.com.erudio.integrationtests.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties
data class AccountCredentialsVO (
    var username: String? = null,
    var password: String? = null
)