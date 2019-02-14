package vn.asiantech.travelmate.extensions

import vn.asiantech.travelmate.utils.Constant
import java.text.SimpleDateFormat
import java.util.*

fun Long.toDateFormat(): String = SimpleDateFormat(Constant.SIMPLE_DAY_FORMAT, Locale.US).format(
    Calendar.getInstance().apply {
        timeInMillis = this@toDateFormat * 1000
    }.time
)
