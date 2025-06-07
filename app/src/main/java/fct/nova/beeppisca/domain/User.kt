package fct.nova.beeppisca.domain

data class User(
    val name: String,
    val email: String,
    val password: String, // Ideally should be encrypted
    val tickets: List<Ticket>,
    val momentLocation: MomentLocation,
    val isAdmin: Boolean = false,
)