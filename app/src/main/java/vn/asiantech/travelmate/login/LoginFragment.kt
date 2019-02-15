package vn.asiantech.travelmate.login

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
import vn.asiantech.travelmate.utils.ValidationUtil

class LoginFragment : Fragment(), View.OnClickListener {
    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
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
                add(R.id.fragment_container, SignUpFragment())
                addToBackStack(null)
                commit()
            }
        } else {
            if (ValidationUtil.isValidEmail(edtEmail?.text.toString().trim()) && ValidationUtil.isValidPassword(edtPassword?.text.toString().trim())) {
                firebaseAuth?.signInWithEmailAndPassword(edtEmail?.text.toString().trim(), edtPassword?.text.toString().trim())
                    ?.addOnCompleteListener { task: Task<AuthResult> ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, getString(R.string.loginFragmentSuccessful), Toast.LENGTH_SHORT)
                                .show()
                            activity?.finish()
                            val intent = Intent(activity, ProfileActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(context, getString(R.string.loginfragmentCannotLogin), Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, getString(R.string.loginfragmentCannotLogin), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
