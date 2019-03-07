package vn.asiantech.travelmate.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.popularcityactivity.PopularCityActivity
import vn.asiantech.travelmate.utils.Constant

class LoginActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null

    companion object {
        private const val KEY_SAVE_VALUE = "value"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
        setContentView(R.layout.activity_login)
        initFragment()
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
        Log.i("bbbb", isSharedPreferences.getBoolean(KEY_SAVE_VALUE, false).toString())
        if (isSharedPreferences.getBoolean(KEY_SAVE_VALUE, false)) {
            val homeIntent = Intent(this, PopularCityActivity::class.java)
            startActivity(homeIntent)
            finish()
        }
    }
}
