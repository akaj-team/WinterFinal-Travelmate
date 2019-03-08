package vn.asiantech.travelmate.login

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_login.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.popularcityactivity.PopularCityActivity
import vn.asiantech.travelmate.utils.Constant

class LoginActivity : AppCompatActivity(), View.OnTouchListener {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        hideKeyBoard()
        return true
    }

    private fun hideKeyBoard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private var progressDialog: ProgressDialog? = null

    companion object {
        private const val KEY_SAVE_VALUE = "value"
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

    override fun onStart() {
        super.onStart()
        val isSharedPreferences: SharedPreferences = getSharedPreferences(Constant.FILE_NAME, Context.MODE_PRIVATE)
        if (isSharedPreferences.getBoolean(KEY_SAVE_VALUE, false)) {
            startActivity(Intent(this@LoginActivity, PopularCityActivity::class.java))
        }
    }
}
