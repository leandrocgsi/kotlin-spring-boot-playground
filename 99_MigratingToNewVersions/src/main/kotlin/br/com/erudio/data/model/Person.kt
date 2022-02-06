package br.com.erudio.data.model

import java.io.Serializable
import jakarta.persistence.*

@Entity
@Table(name = "person")
class Person : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "first_name", nullable = false, length = 80)
    val firstName: String? = null

    @Column(name = "last_name", nullable = false, length = 80)
    val lastName: String? = null

    @Column(nullable = false, length = 100)
    val address: String? = null

    @Column(nullable = false, length = 6)
    val gender: String? = null

    @Column(nullable = false)
    val enabled: Boolean? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}