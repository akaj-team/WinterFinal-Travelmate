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
import vn.asiantech.travelmate.utils.Validate

class SignUpFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_sign_up, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        view?.tvLogin?.setOnClickListener(this)
        view?.btnSignUp?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btnSignUp) {
            if (checkUserPassEmail() == "pass") {
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
        val validate = Validate()
        val firstName = edtFirstName.text.toString().trim()
        val lastName = edtLastName.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val confirmPassword = edtConfirmPassword.text.toString().trim()
        return if (!validate.isValidFirstName(firstName)) {
            getString(R.string.signupTvFirstNameFormatWrong)
        } else if (!validate.isValidLastName(lastName)) {
            getString(R.string.signupTvLastNameFormatWrong)
        } else if (!validate.isValidEmail(email)) {
            getString(R.string.emailFormatWrong)
        } else if (!validate.isValidPassword(password)) {
            getString(R.string.signupTvPasswordFormatWrong)
        } else if (password != confirmPassword) {
            getString(R.string.signupTvConfirmPasswordWrong)
        } else {
            "pass"
        }
    }

    private fun showMessage(message: String) {
        val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)
        val handler = Handler()
        if (tvMessageError.visibility == View.INVISIBLE) {
            tvMessageError.visibility = View.VISIBLE
            tvMessageError.text = message
            tvMessageError.startAnimation(slideUp)
            handler.postDelayed({
                tvMessageError.startAnimation(slideDown)
                tvMessageError.visibility = View.INVISIBLE
            }, 3000)
        }
    }
}
