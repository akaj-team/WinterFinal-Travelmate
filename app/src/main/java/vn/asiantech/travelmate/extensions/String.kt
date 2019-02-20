package vn.asiantech.travelmate.extensions

import android.widget.EditText

fun EditText.getInputText() = this.text.toString().trim()
