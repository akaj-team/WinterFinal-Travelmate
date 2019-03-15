package vn.asiantech.travelmate.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.extensions.getInputText
import vn.asiantech.travelmate.popularcityactivity.PopularCityActivity
import vn.asiantech.travelmate.utils.Constant

class LoginFragment : Fragment(), View.OnClickListener {
    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    companion object {
        private const val IS_LOGIN = "isLogin"
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvRegister.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.tvRegister) {
            fragmentManager?.beginTransaction()?.apply {
                setCustomAnimations(
                    R.anim.right_to_left1,
                    R.anim.right_to_left2,
                    R.anim.left_to_right1,
                    R.anim.left_to_right2
                )
                replace(R.id.fragment_container, SignUpFragment())
                commit()
            }
        } else {
            val emailLogin = edtEmail.getInputText()
            val passwordLogin = edtPassword.getInputText()
            if (!emailLogin.isEmpty() && !passwordLogin.isEmpty()) {
                (activity as? LoginActivity)?.showProgressbarDialog()
                firebaseAuth?.signInWithEmailAndPassword(emailLogin, passwordLogin)
                    ?.addOnCompleteListener { task: Task<AuthResult> ->
                        if (task.isSuccessful) {
                            (activity as? LoginActivity)?.dismissProgressbarDialog()
                            Toast.makeText(context, getString(R.string.loginFragmentSuccessful), Toast.LENGTH_SHORT).show()
                            saveSharedPreferences()
                            startActivity(Intent(activity, PopularCityActivity::class.java))
                            activity?.finish()
                        } else {
                            (activity as? LoginActivity)?.dismissProgressbarDialog()
                            Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, getString(R.string.inputAccount), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveSharedPreferences() {
        activity?.getSharedPreferences(Constant.FILE_NAME, Context.MODE_PRIVATE)?.apply {
            edit()?.apply {
                putBoolean(IS_LOGIN, true)
                apply()
            }
        }
    }
}
