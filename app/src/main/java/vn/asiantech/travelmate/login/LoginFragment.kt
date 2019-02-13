package vn.asiantech.travelmate.login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_login.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.utils.ValidationUtil

class LoginFragment : Fragment(), View.OnClickListener {
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
                setCustomAnimations(R.anim.right_to_left1, R.anim.right_to_left2, R.anim.left_to_right1, R.anim.left_to_right2)
                add(R.id.fragment_container, SignUpFragment())
                addToBackStack(null)
                commit()
            }
        } else {
            if (ValidationUtil.isValidEmail(email = edtEmail?.text.toString().trim()) && ValidationUtil.isValidPassword(password = edtPassword?.text.toString().trim())) {
                Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Can not login", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
