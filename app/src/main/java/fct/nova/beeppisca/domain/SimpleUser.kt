package fct.nova.beeppisca.domain

class SimpleUser(
    val name: String,
    val email: String,
    val password: String?,
    val token: String?,
    val isAdmin: Boolean = false,
)