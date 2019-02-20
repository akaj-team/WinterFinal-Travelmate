package vn.asiantech.travelmate.navigationdrawer

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_setting.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.login.LoginActivity
import vn.asiantech.travelmate.popularcityactivity.PopularCityActivity
import vn.asiantech.travelmate.utils.Constant
import vn.asiantech.travelmate.utils.ValidationUtil

class SettingFragment : Fragment(), View.OnClickListener {
    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var fireBaseUser: FirebaseUser? = firebaseAuth?.currentUser
    private var password: String = ""
    private var selectedPhotoUri : Uri ?= null
    private lateinit var oldPassword: String
    private lateinit var newPassword: String
    private lateinit var confirmPassword: String
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnChangePassword -> {
                val database = FirebaseDatabase.getInstance().getReference(Constant.KEY_ACCOUNT)
                val path = ValidationUtil.getValueChild(fireBaseUser!!.email!!)
                if (checkUserPassEmail() == Constant.CHECK_SIGNUP && !oldPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
                    if (activity is PopularCityActivity) {
                        (activity as PopularCityActivity).showProgressbarDialog()
                        fireBaseUser!!.updatePassword(newPassword).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                database.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        p0.ref.child(Constant.KEY_PASSWORD).setValue(newPassword)
                                    }
                                })


//                                uploadImageToFirebase()


                                firebaseAuth?.signOut()
                                Toast.makeText(context, getString(R.string.successful), Toast.LENGTH_SHORT).show()
                                val intent = Intent(activity, LoginActivity::class.java)
                                startActivity(intent)
                                (activity as PopularCityActivity).progressDialog?.dismiss()
                            } else {
                                Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
                                (activity as PopularCityActivity).progressDialog?.dismiss()
                            }
                        }
                    }
                } else {
                    showMessage(checkUserPassEmail())
                }
            }
            R.id.imgAvatar -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 0)
            }
        }
    }

    /*private fun uploadImageToFirebase() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.i("bbbb", "success : ${it.metadata?.path}")
            }
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity is PopularCityActivity) {
            password = (activity as PopularCityActivity).getPassLogin()
        }
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnChangePassword.setOnClickListener(this)
        imgAvatar.setOnClickListener(this)
    }

    private fun checkUserPassEmail(): String {
        oldPassword = edtOldPassword.text.toString().trim()
        newPassword = edtNewPassword.text.toString().trim()
        confirmPassword = edtConfirmPassword.text.toString().trim()
        return when {
            password != oldPassword -> getString(R.string.passwordWrong)
            !ValidationUtil.isValidPassword(newPassword) -> getString(R.string.signupTvPasswordFormatWrong)
            newPassword != confirmPassword -> getString(R.string.signupTvConfirmPasswordWrong)
            else -> Constant.CHECK_SIGNUP
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.i("bbbb", "selected")
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(resources ,bitmap)
            Log.i("bbbb", bitmapDrawable.toString())
            imgAvatar?.setImageDrawable(bitmapDrawable)
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
