package vn.asiantech.travelmate.utils

import java.util.regex.Pattern

class Validate {
    companion object {
        const val EMAIL_PATTERN =
            "^([a-zA-Z0-9_\\-.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$"
        const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,18}$"
        const val FIRSTNAME_PATTERN = "[A-Z][a-zA-Z]*"
        const val LASTNAME_PATTERN = "[a-zA-z]+([ '-][a-zA-Z]+)*"
    }

    fun isValidEmail(email: String): Boolean {
        val patternEmail = Pattern.compile(EMAIL_PATTERN)
        val matcherEmail = patternEmail.matcher(email)
        return matcherEmail.matches()
    }

    fun isValidPassword(password: String): Boolean {
        val patternPassword = Pattern.compile(PASSWORD_PATTERN)
        val matcherPassword = patternPassword.matcher(password)
        return matcherPassword.matches()
    }

    fun isValidFirstName(firstName: String): Boolean {
        val patternFirstName = Pattern.compile(FIRSTNAME_PATTERN)
        val matcherFirstName = patternFirstName.matcher(firstName)
        return matcherFirstName.matches()
    }

    fun isValidLastName(lastName: String): Boolean {
        val patternLastName = Pattern.compile(LASTNAME_PATTERN)
        val matcherLastName = patternLastName.matcher(lastName)
        return matcherLastName.matches()
    }
}
