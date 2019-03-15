package vn.asiantech.travelmate.login

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.extensions.hideKeyboard
import vn.asiantech.travelmate.popularcityactivity.PopularCityActivity
import vn.asiantech.travelmate.utils.Constant

class LoginActivity : AppCompatActivity(), View.OnTouchListener {
    private var doubleBackToExitPressedOnce = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        view?.hideKeyboard()
        return true
    }

    private var progressDialog: ProgressDialog? = null

    companion object {
        private const val IS_LOGIN = "isLogin"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
        setContentView(R.layout.activity_login)
        initFragment()
        constraintLayout.setOnTouchListener(this)
    }

    private fun initFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }
    fun showProgressbarDialog() {
        progressDialog?.apply {
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            setMessage(getString(R.string.note))
            show()
        }
    }
    fun dismissProgressbarDialog(){
        progressDialog?.dismiss()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment !is LoginFragment) {
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(R.anim.left_to_right1, R.anim.left_to_right2)
                replace(R.id.fragment_container, LoginFragment())
                commit()
            }
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, getString(R.string.backAgainToExit), Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }
    override fun onStart() {
        super.onStart()
        val isSharedPreferences: SharedPreferences = getSharedPreferences(Constant.FILE_NAME, Context.MODE_PRIVATE)
        if (isSharedPreferences.getBoolean(IS_LOGIN, false)) {
            startActivity(Intent(this@LoginActivity, PopularCityActivity::class.java))
            finish()
        }
    }
}
