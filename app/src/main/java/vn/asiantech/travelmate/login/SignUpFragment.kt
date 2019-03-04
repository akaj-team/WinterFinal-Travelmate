package vn.asiantech.travelmate.login

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_sign_up.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.extensions.getInputText
import vn.asiantech.travelmate.models.User
import vn.asiantech.travelmate.utils.Constant
import vn.asiantech.travelmate.utils.ValidationUtil

class SignUpFragment : Fragment(), View.OnClickListener {
    private var fireBaseAuth: FirebaseAuth? = null
    private var firstName: String = ""
    private var lastName: String = ""
    private var email: String = ""
    private var password: String = ""
    private var confirmPassword: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fireBaseAuth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLogin.setOnClickListener(this)
        btnSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btnSignUp) {
            if (checkUserPassEmail() == Constant.CHECK_SIGNUP && !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                (activity as? LoginActivity)?.showProgressbarDialog()
                fireBaseAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        (activity as? LoginActivity)?.dismissProgressbarDialog()
                        val path = ValidationUtil.getValuePathChild(email)
                        val db = FirebaseDatabase.getInstance().getReference(Constant.KEY_ACCOUNT)
                        val courseId = db.push().key
                        val user = User(Constant.URL_AVATAR, firstName, lastName, email, password)
                        courseId?.let { db.child(path).setValue(user) }
                        Toast.makeText(context, getString(R.string.successful), Toast.LENGTH_SHORT).show()
                        resetInputdata()
                    } else {
                        (activity as? LoginActivity)?.dismissProgressbarDialog()
                        Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }
                }
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
        firstName = edtFirstName.getInputText()
        lastName = edtLastName.getInputText()
        email = edtEmail.getInputText()
        password = edtPassword.getInputText()
        confirmPassword = edtConfirmPassword.getInputText()
        return when {
            password != confirmPassword -> getString(R.string.signupTvConfirmPasswordWrong)
            !ValidationUtil.isValidEmail(email) -> getString(R.string.signupEmailFormatWrong)
            !ValidationUtil.isValidFirstName(firstName) -> getString(R.string.signupTvFirstNameFormatWrong)
            !ValidationUtil.isValidLastName(lastName) -> getString(R.string.signupTvLastNameFormatWrong)
            !ValidationUtil.isValidPassword(password) -> getString(R.string.signupTvPasswordFormatWrong)
            else -> Constant.CHECK_SIGNUP
        }
    }

    private fun resetInputdata() {
        edtFirstName?.setText("")
        edtLastName?.setText("")
        edtEmail?.setText("")
        edtPassword?.setText("")
        edtConfirmPassword?.setText("")
    }

    private fun showMessage(message: String) {
        val handler = Handler()
        tvMessageError.apply {
            if (visibility == View.INVISIBLE) {
                visibility = View.VISIBLE
                text = message
                startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
                handler.postDelayed({
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_down))
                    visibility = View.INVISIBLE
                }, 3000)
            }
        }
    }
}
