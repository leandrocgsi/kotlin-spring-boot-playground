package br.com.erudio.data.vo.v1

import java.io.Serializable

class PersonVO : Serializable {

    var id: Long? = null
    var firstName: String? = null
    var lastName: String? = null
    var address: String? = null
    var gender: String? = null
}