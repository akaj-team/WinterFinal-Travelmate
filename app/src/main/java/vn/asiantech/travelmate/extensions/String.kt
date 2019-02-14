package vn.asiantech.travelmate.extensions

import android.widget.EditText

fun String.add(s: String): String {
    return "$this $s"
}

fun EditText.getInputText() = this.text.toString().trim()