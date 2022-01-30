package br.com.erudio.data.model

import java.io.Serializable
import java.util.*
import jakarta.persistence.*

@Entity
@Table(name = "books")
class Book : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "author", nullable = false, length = 180)
    var author: String? = null

    @Column(name = "launch_date", nullable = false)
    @Temporal(TemporalType.DATE)
    var launchDate: Date? = null

    @Column(nullable = false)
    var price: Double? = null

    @Column(nullable = false, length = 250)
    var title: String? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}