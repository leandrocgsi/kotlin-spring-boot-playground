package br.com.erudio.exception

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidJwtAuthenticationException(exception: String?) : AuthenticationException(exception) {
	companion object {
		private const val serialVersionUID = 1L
	}
}