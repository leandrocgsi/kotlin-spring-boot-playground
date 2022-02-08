package br.com.erudio.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UnsuportedMathOperationException(exception: String?) : RuntimeException(exception) {
    companion object {
        private const val serialVersionUID = 1L
    }
}