package vn.asiantech.travelmate.login

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.utils.Validate


class SignUpFragment : Fragment(), View.OnClickListener {
    private var edtFirstName: EditText? = null
    private var edtLastName: EditText? = null
    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var edtConfirmPassword: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_sign_up, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        edtFirstName = view?.findViewById(R.id.edtFirstName)
        edtLastName = view?.findViewById(R.id.edtLastName)
        edtEmail = view?.findViewById(R.id.edtEmail)
        edtPassword = view?.findViewById(R.id.edtPassword)
        edtConfirmPassword = view?.findViewById(R.id.edtConfirmPassword)
        view?.tvLogin?.setOnClickListener(this)
        view?.btnSignUp?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSignUp -> {
                checkUserPassEmail()
            }
            R.id.tvLogin -> {
                val fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.setCustomAnimations(R.anim.left_to_right1, R.anim.left_to_right2)
                fragmentTransaction?.replace(R.id.fragment_container, LoginFragment())
                fragmentTransaction?.commit()
            }
            else -> {
                //nothing
            }
        }
    }

    private fun checkUserPassEmail() {
        val validate = Validate()
        val firstName = edtFirstName?.text.toString().trim()
        val lastName = edtLastName?.text.toString().trim()
        val email = edtEmail?.text.toString().trim()
        val password = edtPassword?.text.toString().trim()
        val confirmPassword = edtConfirmPassword?.text.toString().trim()
        if (!validate.isValidFirstName(firstName)) {
            showMessage(getString(R.string.firstNameFormatWrong))
        } else if (!validate.isValidLastName(lastName)) {
            showMessage(getString(R.string.lastNameFormatWrong))
        } else if (!validate.isValidEmail(email)) {
            showMessage(getString(R.string.emailFormatWrong))
        } else if (!validate.isValidPassword(password)) {
            showMessage(getString(R.string.passwordFormatWrong))
        } else if (password != confirmPassword) {
            showMessage(getString(R.string.confirmPasswordWrong))
        } else {
            Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show()
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
