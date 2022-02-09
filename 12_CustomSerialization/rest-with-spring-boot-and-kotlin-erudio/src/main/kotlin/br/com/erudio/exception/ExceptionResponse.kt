package br.com.erudio.exception

import java.io.Serializable
import java.util.*

class ExceptionResponse(val timestamp: Date, val message: String?, val details: String) : Serializable