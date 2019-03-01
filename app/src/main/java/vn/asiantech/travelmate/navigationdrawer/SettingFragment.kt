package vn.asiantech.travelmate.navigationdrawer

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
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
    private var urlImageNew: String = ""
    private var oldPassword: String = ""
    private var newPassword: String = ""
    private var confirmPassword: String = ""
    private var filePath: Uri? = null
    private var user: User? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    companion object {
        private const val KEY_ACCOUNT: String = "account"
        fun newInstance(user: User) = SettingFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_ACCOUNT, user)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            user = arguments?.getParcelable(KEY_ACCOUNT)
            password = user?.password.toString()
            urlImage = user?.avatar.toString()
        }
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { Glide.with(it).load(user?.avatar).into(imgAvatar) }
        btnChangePassword.setOnClickListener(this)
        imgAvatar.setOnClickListener(this)
    }

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
                eventHandle()
            }
            R.id.btnCancel -> {
            }
        }
    }

    private fun eventHandle() {
        val pictureDialog = AlertDialog.Builder(context)
        pictureDialog.setTitle(getString(R.string.action))
        val pictureDialogItems = arrayOf(getString(R.string.gallery), getString(R.string.camera))
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                Constant.ONCLICK_GALLERY -> if (checkPermissionForGallery()) {
                    chooseGallery()
                }
                Constant.ONCLICK_CAMERA -> if (checkPermissionForCamera()) {
                    chooseCamera()
                }
            }
        }
        pictureDialog.show()
    }

    fun chooseCamera() {
        if (checkPermissionForCamera()) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, Constant.CAMERA)
        }
    }

    fun chooseGallery() {
        if (checkPermissionForGallery()) {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, Constant.GALLERY)
        }
    }
    private fun checkPermissionForCamera(): Boolean {
        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED
            || context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), Constant.REQUEST_ASK_PERMISSION_CAMERA) }
            return false
        }
        return true
    }

    private fun checkPermissionForGallery(): Boolean {
        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constant.REQUEST_ASK_PERMISSION_GALLERY) }
            return false
        }
        return true
    }

    private fun uploadImageToFirebase() {
        if (activity is PopularCityActivity) {
            (activity as PopularCityActivity).showProgressbarDialog()
            storage = FirebaseStorage.getInstance()
            storageReference = storage?.reference
            filePath?.let { temp ->
                val ref = storageReference?.child("images/" + UUID.randomUUID().toString())
                ref?.putFile(temp)
                    ?.addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener { taskSnapShot ->
                            (activity as PopularCityActivity).progressDialog?.dismiss()
                            urlImageNew = taskSnapShot.toString()
                            changePassAndAvatar()
                        }
                    }
                    ?.addOnFailureListener { }
            }
            if (urlImageNew == "") {
                urlImageNew = urlImage
                changePassAndAvatar()
            }
        }
    }

    private fun changePassAndAvatar() {
        val database = FirebaseDatabase.getInstance().getReference(Constant.KEY_ACCOUNT)
        val path = ValidationUtil.getValuePathChild(fireBaseUser!!.email!!)
        fireBaseUser.updatePassword(newPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                database.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        (activity as PopularCityActivity).progressDialog?.dismiss()
                        dataSnapshot.ref.child(Constant.KEY_IMAGE).setValue(urlImageNew)
                        dataSnapshot.ref.child(Constant.KEY_PASSWORD).setValue(newPassword)
                    }
                })
                firebaseAuth?.signOut()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(context, getString(R.string.successful), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.GALLERY) {
                openGallery(data)
            } else {
                openCamera(data)
            }
        }
    }

    private fun openGallery(data: Intent?) {
        filePath = data?.data
        val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
        imgAvatar.setImageBitmap(bitmap)
    }

    private fun openCamera(data: Intent?) {
        val bundle = data?.extras
        val imageBitmap: Bitmap?
        imageBitmap = bundle?.get(getString(R.string.data)) as Bitmap
        val path = MediaStore.Images.Media.insertImage(activity?.contentResolver, imageBitmap, "Title", null)
        filePath = Uri.parse(path)
        imgAvatar.setImageBitmap(imageBitmap)
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
        tvMessageError.apply {
            if (visibility == View.INVISIBLE) {
                visibility = View.VISIBLE
                text = message
                startAnimation(slideUp)
                handler.postDelayed({
                    startAnimation(slideDown)
                    visibility = View.INVISIBLE
                }, 3000)
            }
        }
    }
}
