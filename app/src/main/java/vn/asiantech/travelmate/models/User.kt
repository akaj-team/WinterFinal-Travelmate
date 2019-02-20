package vn.asiantech.travelmate.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var avatar: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var email: String? = "",
    var password: String = ""
)
