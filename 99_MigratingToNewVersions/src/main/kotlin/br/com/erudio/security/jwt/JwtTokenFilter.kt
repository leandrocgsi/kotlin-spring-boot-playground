package br.com.erudio.security.jwt

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest

class JwtTokenFilter(@field:Autowired private val tokenProvider: JwtTokenProvider) : GenericFilterBean() {
	//@Throws(IOException::class, ServletException::class)
	override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
		val token: String = tokenProvider.resolveToken(request as HttpServletRequest)
		if (token != null && tokenProvider.validateToken(token)) {
			val auth: Authentication = tokenProvider.getAuthentication(token)
			if (auth != null) {
				SecurityContextHolder.getContext().authentication = auth
			}
		}
		chain.doFilter(request, response)
	}
}