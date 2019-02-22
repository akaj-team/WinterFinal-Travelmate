package vn.asiantech.travelmate.navigationdrawer

import android.app.Activity
import android.app.ProgressDialog
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
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_setting.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.login.LoginActivity
import vn.asiantech.travelmate.popularcityactivity.PopularCityActivity
import vn.asiantech.travelmate.utils.Constant
import vn.asiantech.travelmate.utils.ValidationUtil
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.math.roundToInt


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class SettingFragment : Fragment(), View.OnClickListener {
    private val firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private val fireBaseUser: FirebaseUser? = firebaseAuth?.currentUser
    private var password: String = ""
    private var urlImage: String = ""
    private var filePath : Uri ?= null
    private var storage : FirebaseStorage ?= null
    private var storageReference : StorageReference ?= null
    private lateinit var oldPassword: String
    private lateinit var newPassword: String
    private lateinit var confirmPassword: String
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnChangePassword -> {
                /*val database = FirebaseDatabase.getInstance().getReference(Constant.KEY_ACCOUNT)
                val path = ValidationUtil.getValueChild(fireBaseUser!!.email!!)*/
                /*if (checkUserPassEmail() == Constant.CHECK_SIGNUP && !oldPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
                    uploadImageToFirebase()
                    *//*if (activity is PopularCityActivity) {
                        (activity as PopularCityActivity).showProgressbarDialog()
                        fireBaseUser.updatePassword(newPassword).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                database.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {

                                        p0.ref.child(Constant.KEY_PASSWORD).setValue(*//**//*newPassword*//**//*urlImage)
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
                    }*//*
                } else {
                    showMessage(checkUserPassEmail())
                }*/
                uploadImageToFirebase()
            }
            R.id.imgAvatar -> {
                chooseImage()
            }
        }
    }

    private fun uploadImageToFirebase() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Uploading ...")
        progressDialog.show()

        storage = FirebaseStorage.getInstance()
        storageReference = storage?.getReference()
        if (filePath != null) {
            val ref = storageReference?.child("images/"+ UUID.randomUUID().toString())!!
            ref.putFile(filePath!!)
                .addOnSuccessListener( object : OnSuccessListener<UploadTask.TaskSnapshot>{
                    override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
                        progressDialog.dismiss()
                        urlImage = taskSnapshot?.metadata?.reference?.downloadUrl.toString()
//                        changePass()
                        Log.i("bbbb", urlImage)
//                        val url = storageReference?.child("image/"+ ref.path)
                        Toast.makeText(context, urlImage, Toast.LENGTH_SHORT).show()
//                        var imageUrl = p0?.uploadSessionUri
                    } } )
                .addOnFailureListener ( object : OnFailureListener{
                    override fun onFailure(exception : Exception) {
                        progressDialog.dismiss()
                        Log.i("bbbb", "fail")
                    }

                } )
                .addOnProgressListener (
                    object : OnProgressListener<UploadTask.TaskSnapshot> {
                        override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot?) {
                            val process = (100 * taskSnapshot!!.bytesTransferred / taskSnapshot.totalByteCount)
                            progressDialog.setMessage("Upload " + process + "%")
                        }
                    }
                        )
                }
    }

   /* private fun changePass() {
        val database = FirebaseDatabase.getInstance().getReference(Constant.KEY_ACCOUNT)
        val path = ValidationUtil.getValueChild(fireBaseUser!!.email!!)
        if (activity is PopularCityActivity) {
            (activity as PopularCityActivity).showProgressbarDialog()
            fireBaseUser.updatePassword(newPassword).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    database.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            p0.ref.child(Constant.KEY_PASSWORD).setValue(*//*newPassword*//*urlImage)
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
    }*/

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
            /*try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
                imgAvatar.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }*/
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
            imgAvatar.setImageBitmap(bitmap)
        }
    }

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
