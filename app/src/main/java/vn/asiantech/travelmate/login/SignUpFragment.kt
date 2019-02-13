package vn.asiantech.travelmate.login

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.utils.ValidationUtil

class SignUpFragment : Fragment(), View.OnClickListener {
    companion object {
        const val CHECK_SIGNUP = "OK"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_sign_up, container, false)
        view.tvLogin.setOnClickListener(this)
        view.btnSignUp.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btnSignUp) {
            if (checkUserPassEmail() == CHECK_SIGNUP) {
                Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show()
            } else {
                showMessage(checkUserPassEmail())
            }

        } else {
            fragmentManager?.beginTransaction()?.apply {
                setCustomAnimations(R.anim.left_to_right1, R.anim.left_to_right2)
                replace(R.id.fragment_container, LoginFragment())
                commit()
            }
        }
    }

    private fun checkUserPassEmail(): String {
        val validate = ValidationUtil
        val firstName = edtFirstName.text.toString().trim()
        val lastName = edtLastName.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val confirmPassword = edtConfirmPassword.text.toString().trim()
        return when {
            password != confirmPassword -> getString(R.string.signupTvConfirmPasswordWrong)
            !validate.isValidEmail(email) -> getString(R.string.signupEmailFormatWrong)
            !validate.isValidFirstName(firstName) -> getString(R.string.signupTvFirstNameFormatWrong)
            !validate.isValidLastName(lastName) -> getString(R.string.signupTvLastNameFormatWrong)
            !validate.isValidPassword(password) -> getString(R.string.signupTvPasswordFormatWrong)
            else -> CHECK_SIGNUP
        }
    }

    private fun showMessage(message: String) {
        val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)
        val handler = Handler()
        if (tvMessageError.visibility == View.INVISIBLE) {
            tvMessageError?.apply {
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
