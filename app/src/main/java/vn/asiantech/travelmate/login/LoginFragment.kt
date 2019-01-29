package vn.asiantech.travelmate.login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_login.view.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.utils.Validate

class LoginFragment : Fragment(), View.OnClickListener {
    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        edtEmail = view?.findViewById(R.id.edtEmail)
        edtPassword = view?.findViewById(R.id.edtPassword)
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

                } else {
                    Toast.makeText(context, "Can not login", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                //nothing
            }
        }
    }
}
