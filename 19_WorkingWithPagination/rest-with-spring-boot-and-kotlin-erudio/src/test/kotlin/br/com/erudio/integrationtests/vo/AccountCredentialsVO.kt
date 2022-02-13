package br.com.erudio.integrationtests.vo

import jakarta.xml.bind.annotation.XmlRootElement
import java.io.Serializable

@XmlRootElement
class AccountCredentialsVO : Serializable {
    var username: String? = null
    var password: String? = null
}