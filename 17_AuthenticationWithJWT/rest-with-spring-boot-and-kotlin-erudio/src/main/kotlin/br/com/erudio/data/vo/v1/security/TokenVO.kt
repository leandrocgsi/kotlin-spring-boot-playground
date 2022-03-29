package br.com.erudio.data.vo.v1.security

import java.util.*

data class TokenVO (
    var username: String? = null,
    var authenticated: Boolean? = null,
    var created: Date? = null,
    var expiration: Date? = null,
    var accessToken: String? = null,
    var refreshToken: String? = null,
)