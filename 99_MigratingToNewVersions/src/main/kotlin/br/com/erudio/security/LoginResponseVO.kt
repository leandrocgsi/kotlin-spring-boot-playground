package br.com.erudio.security

import java.io.Serializable

class LoginResponseVO : Serializable {
	var username: String? = null
	var token: String? = null

	constructor() {}
	constructor(username: String?, token: String?) {
		this.username = username
		this.token = token
	}

	companion object {
		private const val serialVersionUID = 1L
	}
}