package br.com.erudio.integrationtests.vo

import jakarta.xml.bind.annotation.XmlRootElement
import java.io.Serializable
import java.util.*

@XmlRootElement
class TokenVO : Serializable {
    var username: String? = null
    var authenticated: Boolean? = null
    var created: Date? = null
    var expiration: Date? = null
    var accessToken: String? = null
    var refreshToken: String? = null

    /**constructor() {}
    constructor(
        username: String?, authenticated: Boolean?, created: Date?, expiration: Date?, accessToken: String?,
        refreshToken: String?
    ) {
        this.username = username
        this.authenticated = authenticated
        this.created = created
        this.expiration = expiration
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }*/
}