package fct.nova.beeppisca.domain

sealed class Ticket {
    abstract val id: String
    abstract val type: String
    abstract val dateOfPurchase: String
    abstract val expirationDate: String
    abstract val isValid: Boolean

    data class StandaloneTicket(
        override val id: String,
        override val type: String = "Stand-Alone",
        override val dateOfPurchase: String,
        override val expirationDate: String,
        val isUsed: Boolean = false
    ) : Ticket() {
        override val isValid: Boolean
            get() = !isUsed
    }

    data class MonthlyTicket(
        override val id: String,
        override val type: String = "Monthly",
        override val dateOfPurchase: String,
        override val expirationDate: String,
        val remainingUses: Int = Int.MAX_VALUE // Unlimited uses until the month ends
    ) : Ticket() {
        override val isValid: Boolean
            get() = expirationDate > getCurrentDate()
    }

    internal fun getCurrentDate(): String {
        // Helper function to get the current date in a comparable string format (yyyy-MM-dd)
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

}
