package br.com.erudio.security.jwt

import br.com.erudio.exception.InvalidJwtAuthenticationException
import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest

@Service
class JwtTokenProvider {
	private var secretKey = "secret"
	private val validityInMilliseconds: Long = 3600000 //1h

	@Autowired
	private val userDetailsService: UserDetailsService? = null
	@PostConstruct
	protected fun init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
	}

	fun createToken(username: String?, roles: List<String?>): String {
		val claims = Jwts.claims().setSubject(username)
		claims.put("roles", roles)
		val now = Date()
		val validity = Date(now.time + validityInMilliseconds)
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact()
	}

	fun getAuthentication(token: String): Authentication {
		val userDetails = userDetailsService!!.loadUserByUsername(getUsername(token))
		return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
	}

	private fun getUsername(token: String): String {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
	}

	fun resolveToken(req: HttpServletRequest): String? {
		val bearerToken = req.getHeader("Authorization")
		return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			bearerToken.substring(7, bearerToken.length)
		} else null
	}

	fun validateToken(token: String?): Boolean {
		return try {
			val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
			if (claims.body.expiration.before(Date())) {
				false
			} else true
		} catch (e: JwtException) {
			throw InvalidJwtAuthenticationException("Expired or invalid JWT token")
		} catch (e: IllegalArgumentException) {
			throw InvalidJwtAuthenticationException("Expired or invalid JWT token")
		}
	}
}