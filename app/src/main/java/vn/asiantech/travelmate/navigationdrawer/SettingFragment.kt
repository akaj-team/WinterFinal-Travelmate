package vn.asiantech.travelmate.navigationdrawer

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_setting.*

import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.login.LoginActivity
import vn.asiantech.travelmate.models.User
import vn.asiantech.travelmate.utils.Constant

class SettingFragment : Fragment(), View.OnClickListener {
    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var fireBaseUser: FirebaseUser? = firebaseAuth?.currentUser
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnChangePassword -> {
                val txtNewPass = edtNewPassword.text.toString()
                val db = FirebaseDatabase.getInstance().getReference(Constant.KEY_ACCOUNT)

                /*if(txtNewPass != ""){
                    fireBaseUser!!.updatePassword(txtNewPass).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            firebaseAuth?.signOut()
                            val intent = Intent(activity, LoginActivity::class.java)
                            startActivity(intent)
                            Log.i("cccc", "Update Success")
                        } else {
                            Log.i("cccc", "Update Error")
                        }
                    }
                }*/
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnChangePassword.setOnClickListener(this)
    }
}
