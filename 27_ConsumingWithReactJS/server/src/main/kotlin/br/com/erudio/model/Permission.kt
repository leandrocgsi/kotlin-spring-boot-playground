package br.com.erudio.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority

@Entity
@Table(name = "permission")
class Permission : GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "description")
    var description: String? = null

    override fun getAuthority(): String {
        return description!!
    }
}