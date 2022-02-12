package br.com.erudio.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import br.com.erudio.data.vo.v1.security.AccountCredentialsVO
import br.com.erudio.data.vo.v1.security.TokenVO
import br.com.erudio.repository.UserRepository
import br.com.erudio.security.jwt.JwtTokenProvider


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
            val username = data.username
            val password = data.password
            authenticationManager!!.authenticate(UsernamePasswordAuthenticationToken(username, password))
            val user = repository!!.findByUsername(username)
            var tokenResponse = TokenVO()
            tokenResponse = if (user != null) {
                tokenProvider!!.createAccessToken(username!!, user.roles)
            } else {
                throw UsernameNotFoundException("Username $username not found!")
            }
            ResponseEntity.ok(tokenResponse)
        } catch (e: AuthenticationException) {
            throw BadCredentialsException("Invalid username/password supplied!")
        }
    }

    fun refreshToken(username: String, refreshToken: String?): ResponseEntity<*> {
        val user = repository!!.findByUsername(username)
        var tokenResponse = TokenVO()
        tokenResponse = if (user != null) {
            tokenProvider!!.refreshToken(refreshToken!!)
        } else {
            throw UsernameNotFoundException("Username $username not found!")
        }
        return ResponseEntity.ok(tokenResponse)
    }
}