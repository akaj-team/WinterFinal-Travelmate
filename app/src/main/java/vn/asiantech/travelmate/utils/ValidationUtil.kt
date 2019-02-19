package vn.asiantech.travelmate.utils

import java.util.regex.Pattern

object ValidationUtil {
    private const val EMAIL_PATTERN = "^([a-zA-Z0-9_\\-.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$"
    private const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,18}$"
    private const val FIRSTNAME_PATTERN = "[a-zA-Z][a-zA-Z]*"
    private const val LASTNAME_PATTERN = "[a-zA-z]+([ '-][a-zA-Z]+)*"

    fun isValidEmail(email: String): Boolean = Pattern.compile(EMAIL_PATTERN).matcher(email).matches()
    fun isValidPassword(password: String): Boolean = Pattern.compile(PASSWORD_PATTERN).matcher(password).matches()
    fun isValidFirstName(firstName: String): Boolean = Pattern.compile(FIRSTNAME_PATTERN).matcher(firstName).matches()
    fun isValidLastName(lastName: String): Boolean = Pattern.compile(LASTNAME_PATTERN).matcher(lastName).matches()

    // field containt information account userlogin
    fun valuePathChild(email : String) : String{
        val result = email.replace(".", "_")
        val pathChild = result.replace("@", "_")
        return pathChild
    }
}
