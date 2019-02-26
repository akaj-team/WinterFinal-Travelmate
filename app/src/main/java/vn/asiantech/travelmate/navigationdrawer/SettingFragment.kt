package vn.asiantech.travelmate.navigationdrawer

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
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
    private var oldPassword: String = ""
    private var newPassword: String = ""
    private var confirmPassword: String = ""
    private var filePath: Uri? = null
    private var user: User? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private val onClickGallery = 0
    private val onClickCamera = 1
    private val gallery = 111
    private val camera = 222
    private val requestAskPermissionCamera = 333
    private val requestAskPermissionGallery = 444

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
//                chooseImage()
            }
            R.id.btnCancel -> {

            }
        }
    }

    private fun eventHandle() {
        val pictureDialog = AlertDialog.Builder(context)
        pictureDialog.setTitle("please choose")
        val pictureDialogItems = arrayOf("gallery", "camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                onClickGallery -> if (checkPermissionForGallery()) {
                    chooseGallery()
                }
                onClickCamera -> if (checkPermissionForCamera()) {
                    chooseCamera()
                }
            }
        }
        pictureDialog.show()
    }

    private fun chooseCamera() {
        if (checkPermissionForCamera()) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, camera)
        }
    }

    private fun chooseGallery() {
        if (checkPermissionForGallery()) {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, gallery)
        }
    }

    private fun checkPermissionForCamera(): Boolean {
        if (!(context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            } == PackageManager.PERMISSION_GRANTED
                    || context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.CAMERA
                )
            } == PackageManager.PERMISSION_GRANTED)) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    requestAskPermissionCamera
                )
            }
            return false
        }
        return true
    }

    private fun checkPermissionForGallery(): Boolean {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            } != PackageManager.PERMISSION_GRANTED) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    requestAskPermissionGallery
                )
            }
            return false
        }
        return true
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
        progressDialog.setTitle(getString(R.string.uploading))
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val isShowRationaleWrite: Boolean
        when (requestCode) {
            requestAskPermissionCamera -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    chooseCamera()
                } else {
                    val isShowRationaleCamera =
                        activity?.let {
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                it,
                                Manifest.permission.CAMERA
                            )
                        }
                    isShowRationaleWrite =
                        activity?.let {
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                it,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        }!!
                    if (!isShowRationaleWrite || !isShowRationaleCamera!!) {
                        showSettingsAlert(getString(R.string.noteCamera))
                    }
                }
            }
            requestAskPermissionGallery -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseGallery()
                } else {
                    isShowRationaleWrite =
                        activity?.let {
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                it,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        }!!
                    if (!isShowRationaleWrite) {
                        showSettingsAlert(getString(R.string.noteGallery))
                    }
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun showSettingsAlert(message: String) {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(getString(R.string.optionChoose))
        alertDialog.setMessage(getString(R.string.noteAccess) + " " + message)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, getString(R.string.setting)
        ) { dialog, _ ->
            dialog.dismiss()
            startInstalledAppDetailsActivity(activity)
        }
        alertDialog.show()
    }

    private fun startInstalledAppDetailsActivity(context: Activity?) {
        if (context == null) {
            return
        }
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:" + context.packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        context.startActivity(intent)
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
