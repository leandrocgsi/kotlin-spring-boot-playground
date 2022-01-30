package br.com.erudio.data.model

import java.io.Serializable
import jakarta.persistence.*

@Entity
@Table(name = "person")
class Person : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "first_name", nullable = false, length = 80)
    var firstName: String? = null

    @Column(name = "last_name", nullable = false, length = 80)
    var lastName: String? = null

    @Column(nullable = false, length = 100)
    var address: String? = null

    @Column(nullable = false, length = 6)
    var gender: String? = null

    @Column(nullable = false)
    var enabled: Boolean? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}