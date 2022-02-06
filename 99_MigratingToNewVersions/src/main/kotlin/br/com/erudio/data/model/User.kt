package br.com.erudio.data.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable
import java.util.*
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User : UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null

    @Column(name = "user_name", unique = true)
    val userName: String? = null

    @Column(name = "full_name")
    val fullName: String? = null

    @Column(name = "password")
    private val password: String? = null

    @Column(name = "account_non_expired")
    val accountNonExpired: Boolean? = null

    @Column(name = "account_non_locked")
    val accountNonLocked: Boolean? = null

    @Column(name = "credentials_non_expired")
    val credentialsNonExpired: Boolean? = null

    @Column(name = "enabled")
    val enabled: Boolean? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_permission",
        joinColumns = [JoinColumn(name = "id_user")],
        inverseJoinColumns = [JoinColumn(name = "id_permission")]
    )
    val permissions: List<Permission?>? = null
    val roles: List<String?>
        get() {
            val roles: MutableList<String?> = ArrayList()
            for (permission in permissions!!) {
                roles.add(permission!!.description)
            }
            return roles
        }

    fun setPassword(password: String?) {
        this.password = password
    }

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return permissions!!
    }

    override fun getPassword(): String {
        return password!!
    }

    override fun getUsername(): String {
        return userName!!
    }

    override fun isAccountNonExpired(): Boolean {
        return accountNonExpired!!
    }

    override fun isAccountNonLocked(): Boolean {
        return accountNonLocked!!
    }

    override fun isCredentialsNonExpired(): Boolean {
        return credentialsNonExpired!!
    }

    override fun isEnabled(): Boolean {
        return enabled!!
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}