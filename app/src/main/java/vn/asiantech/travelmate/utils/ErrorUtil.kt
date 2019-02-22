package vn.asiantech.travelmate.utils

import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.widget.TextView

object ErrorUtil {
    fun showMessage(tvError: TextView, message: String, slideUp: Animation, slideDown: Animation) {
        val handler = Handler()
        tvError.apply {
            if (visibility == View.INVISIBLE) {
                visibility = View.VISIBLE
                text = message
                startAnimation(slideUp)
                handler.postDelayed({
                    startAnimation(slideDown)
                    visibility = View.INVISIBLE
                }, 3000)
            }
        }
    }
}
