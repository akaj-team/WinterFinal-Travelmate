package vn.asiantech.travelmate.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import vn.asiantech.travelmate.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initFragment()
    }

    private fun initFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, LoginFragment())
        fragmentTransaction.commit()
    }
}