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
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.extensions.getInputText
import vn.asiantech.travelmate.utils.Constant
import vn.asiantech.travelmate.utils.ValidationUtil

class SignUpFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLogin.setOnClickListener(this)
        btnSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btnSignUp) {
            if (checkUserPassEmail() == Constant.CHECK_SIGNUP) {
                Toast.makeText(context, getString(R.string.signUpFragmentOk), Toast.LENGTH_SHORT).show()
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
        return when {
            edtPassword.getInputText() != edtPassword.getInputText() -> getString(R.string.signupTvConfirmPasswordWrong)
            !ValidationUtil.isValidEmail(edtEmail.getInputText()) -> getString(R.string.signupEmailFormatWrong)
            !ValidationUtil.isValidFirstName(edtFirstName.getInputText()) -> getString(R.string.signupTvFirstNameFormatWrong)
            !ValidationUtil.isValidLastName(edtLastName.getInputText()) -> getString(R.string.signupTvLastNameFormatWrong)
            !ValidationUtil.isValidPassword(edtPassword.getInputText()) -> getString(R.string.signupTvPasswordFormatWrong)
            else -> Constant.CHECK_SIGNUP
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
