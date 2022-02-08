package br.com.erudio.model

import java.io.Serializable

class Person : Serializable {

    var id: Long = 0
    var firstName: String = ""
    var lastName: String = ""
    var address: String = ""
    var gender: String = ""
}