package br.com.erudio.controller

import org.springframework.beans.factory.annotation.Autowired
import br.com.erudio.services.AuthServices
import br.com.erudio.data.vo.v1.security.AccountCredentialsVO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    lateinit var authServices: AuthServices

    @Operation(summary = "Authenticates a user and returns a token")
    @PostMapping(value = ["/signin"])
    fun signin(@RequestBody data: AccountCredentialsVO?): ResponseEntity<*> {
        return if (data?.username.isNullOrBlank() || data?.password.isNullOrBlank())
            ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Invalid client request") else authServices.signin(data!!)
    }

    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @PutMapping(value = ["/refresh/{username}"])
    fun refreshToken(@PathVariable("username") username: String?, @RequestHeader("Authorization") refreshToken: String?): ResponseEntity<*> {
        return if (refreshToken.isNullOrBlank() || username.isNullOrBlank())
            ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Invalid client request") else authServices.refreshToken(username!!, refreshToken)
    }
}