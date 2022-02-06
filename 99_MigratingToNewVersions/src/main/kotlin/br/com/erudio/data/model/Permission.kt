package br.com.erudio.data.model

import org.springframework.security.core.GrantedAuthority
import java.io.Serializable
import jakarta.persistence.*

@Entity
@Table(name = "permission")
class Permission : GrantedAuthority, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null

    @Column(name = "description")
    val description: String? = null

    override fun getAuthority(): String {
        return description!!
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}