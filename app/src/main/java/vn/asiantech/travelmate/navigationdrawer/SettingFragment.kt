package vn.asiantech.travelmate.navigationdrawer

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_setting.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.login.LoginActivity
import vn.asiantech.travelmate.models.User
import vn.asiantech.travelmate.popularcityactivity.PopularCityActivity
import vn.asiantech.travelmate.utils.Constant
import vn.asiantech.travelmate.utils.ValidationUtil
import java.util.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class SettingFragment : Fragment(), View.OnClickListener {
    private val firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private val fireBaseUser: FirebaseUser? = firebaseAuth?.currentUser
    private var password: String = ""
    private var urlImage: String = ""
    private var oldPassword: String = ""
    private var newPassword: String = ""
    private var confirmPassword: String = ""
    private var filePath: Uri? = null
    private var user: User? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnChangePassword -> {
                if (checkUserPassEmail() == Constant.CHECK_SIGNUP && !oldPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
                    uploadImageToFirebase()
                } else {
                    showMessage(checkUserPassEmail())
                }
            }
            R.id.imgAvatar -> {
                chooseImage()
            }
        }
    }

    companion object {
        private const val account: String = "account"
        fun newInstance(user: User): SettingFragment {
            val bundle = Bundle()
            bundle.putParcelable(account, user)
            val fragment = SettingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun uploadImageToFirebase() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Uploading ...")
        progressDialog.show()
        storage = FirebaseStorage.getInstance()
        storageReference = storage?.reference
        filePath?.let { temp ->
            val ref = storageReference?.child("images/" + UUID.randomUUID().toString())
            ref?.putFile(temp)
                ?.addOnSuccessListener {
                    progressDialog.dismiss()
                    ref.downloadUrl.addOnSuccessListener { taskSnapShot ->
                        urlImage = taskSnapShot.toString()
                        changePassAndAvatar()
                        Toast.makeText(context, taskSnapShot.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                ?.addOnFailureListener { progressDialog.dismiss() }
                ?.addOnProgressListener { taskSnapshot ->
                    val process = (100 * taskSnapshot!!.bytesTransferred / taskSnapshot.totalByteCount)
                    progressDialog.setMessage(getString(R.string.upload) + process + getString(R.string.percent))
                }
        }
    }

    private fun changePassAndAvatar() {
        val database = FirebaseDatabase.getInstance().getReference(Constant.KEY_ACCOUNT)
        val path = ValidationUtil.getValuePathChild(fireBaseUser!!.email!!)
        if (activity is PopularCityActivity) {
            (activity as PopularCityActivity).showProgressbarDialog()
            fireBaseUser.updatePassword(newPassword).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    database.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError) {
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.ref.child(Constant.KEY_IMAGE).setValue(urlImage)
                            dataSnapshot.ref.child(Constant.KEY_PASSWORD).setValue(newPassword)
                        }
                    })
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
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constant.PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === Constant.PICK_IMAGE_REQUEST && resultCode === Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            filePath = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
            imgAvatar.setImageBitmap(bitmap)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        arguments?.let {
            user = arguments?.getParcelable(account)
            password = user?.password.toString()
        }
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { Glide.with(it).load(user?.avatar).into(imgAvatar) }
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
