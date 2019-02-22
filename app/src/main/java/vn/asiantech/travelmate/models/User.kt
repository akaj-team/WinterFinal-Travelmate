package vn.asiantech.travelmate.models

data class User(
    var avatar: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var email: String? = "",
    var password: String = ""
)
