package br.com.erudio.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "books")
data class Book (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "author", nullable = false, length = 180)
    var author: String = "",

    @Column(name = "launch_date", nullable = false)
    @Temporal(TemporalType.DATE)
    var launchDate: Date = Date(),

    @Column(nullable = false)
    var price: Double = 0.toDouble(),

    @Column(nullable = false, length = 250)
    var title: String = "",
)