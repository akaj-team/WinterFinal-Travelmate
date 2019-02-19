package vn.asiantech.travelmate.login

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
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
import vn.asiantech.travelmate.models.User
import vn.asiantech.travelmate.utils.Constant
import vn.asiantech.travelmate.utils.ValidationUtil

class SignUpFragment : Fragment(), View.OnClickListener {
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirmPassword: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.fireBaseAuth = FirebaseAuth.getInstance()
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
                if(activity is LoginActivity) {
                    (activity as LoginActivity).showProgressbarDialog()
                    fireBaseAuth.createUserWithEmailAndPassword(edtEmail?.text.toString(), edtPassword?.text.toString())
                        .addOnCompleteListener { task: Task<AuthResult> ->
                            if (task.isSuccessful) {
                                val path = ValidationUtil.getValueChild(email)
                                val db = FirebaseDatabase.getInstance().getReference(Constant.KEY_ACCOUNT)
                                val courseId = db.push().key
                                val user = User(firstName, lastName, email, password)
                                courseId?.let { db.child(path).setValue(user) }
                                Toast.makeText(context, getString(R.string.successful), Toast.LENGTH_SHORT).show()
                                resetInputdata()
                                (activity as LoginActivity).progressDialog?.dismiss()
                            } else {
                                Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
                                (activity as LoginActivity).progressDialog?.dismiss()
                            }
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
        firstName = edtFirstName.text.toString().trim()
        lastName = edtLastName.text.toString().trim()
        email = edtEmail.text.toString().trim()
        password = edtPassword.text.toString().trim()
        confirmPassword = edtConfirmPassword.text.toString().trim()
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
