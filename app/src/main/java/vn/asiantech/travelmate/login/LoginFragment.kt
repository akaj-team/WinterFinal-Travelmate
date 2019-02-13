package vn.asiantech.travelmate.login

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.utils.Validate

class LoginFragment : Fragment(), View.OnClickListener {
    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        actionHandler(view)
        return view
    }

    private fun actionHandler(view: View?) {
        view?.tvRegister?.setOnClickListener(this)
        view?.btnLogin?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val validate = Validate()
        when (v?.id) {
            R.id.tvRegister -> {
                val fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.setCustomAnimations(
                    R.anim.right_to_left1,
                    R.anim.right_to_left2,
                    R.anim.left_to_right1,
                    R.anim.left_to_right2
                )
                fragmentTransaction?.add(R.id.fragment_container, SignUpFragment())
                fragmentTransaction?.addToBackStack("ok")
                fragmentTransaction?.commit()
            }
            R.id.btnLogin -> {
                if (validate.isValidEmail(email = edtEmail?.text.toString().trim()) && validate.isValidPassword(password = edtPassword?.text.toString().trim())) {
                    firebaseAuth?.signInWithEmailAndPassword(
                        edtEmail?.text.toString().trim(),
                        edtPassword?.text.toString().trim()
                    )
                        ?.addOnCompleteListener { task: Task<AuthResult> ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, getString(R.string.successful), Toast.LENGTH_SHORT).show()
                                activity?.finish()
                                val intent = Intent(activity, ProfileActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                //nothing
            }
        }
    }
}
