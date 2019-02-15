package vn.asiantech.travelmate.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import vn.asiantech.travelmate.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }
}