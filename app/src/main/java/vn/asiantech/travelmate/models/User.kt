package vn.asiantech.travelmate.models

data class User(
    var avatar : String ? = "",
    var firstName: String ? = "",
    var lastName: String ?= "",
    var email: String ?= "",
    val password : String ?= ""
)
/*
class User {
    var avatar: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var password: String? = null

    constructor(avatar: String?, firstName: String?, lastName: String?, email: String?, password: String?) {
        this.avatar = avatar
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.password = password
    }
}
*/
