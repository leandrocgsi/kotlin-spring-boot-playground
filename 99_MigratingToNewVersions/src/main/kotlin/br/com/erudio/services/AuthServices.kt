package br.com.erudio.services

import br.com.erudio.repository.UserRepository
import br.com.erudio.security.AccountCredentialsVO
import br.com.erudio.security.LoginResponseVO
import br.com.erudio.security.jwt.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthServices {
	@Autowired
	var authenticationManager: AuthenticationManager? = null

	@Autowired
	var tokenProvider: JwtTokenProvider? = null

	@Autowired
	var repository: UserRepository? = null
	fun signin(data: AccountCredentialsVO): ResponseEntity<*> {
		return try {
			val username: Unit = data.getUsername()
			val pasword: Unit = data.getPassword()
			authenticationManager!!.authenticate(UsernamePasswordAuthenticationToken(username, pasword))
			val user = repository!!.findByUsername(username)
			val loginResponse = LoginResponseVO()
			if (user != null) {
				loginResponse.setToken(tokenProvider!!.createToken(username, user.roles))
				loginResponse.setUsername(username)
			} else {
				throw UsernameNotFoundException("Username $username not found!")
			}
			ResponseEntity.ok(loginResponse)
		} catch (e: AuthenticationException) {
			throw BadCredentialsException("Invalid username/password supplied!")
		}
	}
}