package fct.nova.beeppisca.service

object URIS {
    object Base {
        private const val ROOT = "https://localhost:8080"
        private const val API = "$ROOT/api"
        const val VERSION = "$API/v1"
        const val HEALTH_CHECK = "$ROOT/health"
    }

    object User {
        const val ROOT = "${Base.VERSION}/user"
        const val CREATE = "/create"
        const val LOGIN = "/login"
        const val LOGOUT = "/logout"
        const val BY_ID = "/{id}"
        const val ME = "/me"
    }

    object NavigationCard {
        const val ROOT = "${Base.VERSION}/navigation-card"
        const val CREATE = "/create"
        const val BY_ID = "/{id}"
        const val MY_CARDS = "/my-cards"
        const val VALIDATE = "/validate"
        const val GET_NEW_AVAILABLE_CARDS = "/get-new-available-cards"
        const val PURCHASE = "/buy"
        const val HAS_MONTHLY = "/has-monthly"
        const val HAS_REGULAR = "/has-regular"
    }

    object Route {
        const val ROOT = "${Base.VERSION}/routes"
        const val BY_ID = "/{id}"
    }

    object Bus {
        const val ROOT = "${Base.VERSION}/buses"
        const val BY_ID = "/{id}"
        const val BY_ROUTE = "/route/{routeId}"
    }

    object Position {
        const val ROOT = "${Base.VERSION}/positions"
        const val ADD_BUS = "/bus/{busId}"
        const val ADD_USER = "/user/{userId}"
        const val ADD_COMBINED = "/combined"
        const val LATEST_BUS = "/bus/{busId}/latest"
        const val LATEST_USER = "/user/{userId}/latest"
        const val IS_USER_INSIDE = "/is-user-inside"
        const val BUS_TO_USER_DISTANCE = "/bus-to-user"
        const val IS_USER_NEAR_BUS_STOP = "/is-user-near-bus-stop"
    }

    object Events {
        const val ROOT = "${Base.VERSION}/events"
        const val VALIDATE_CARD = "/validate"
    }
}
