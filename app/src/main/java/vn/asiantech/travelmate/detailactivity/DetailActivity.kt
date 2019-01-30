package vn.asiantech.travelmate.detailactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import vn.asiantech.travelmate.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initFragment()
    }

    private fun initFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, DetailFragment())
        fragmentTransaction.commit()
    }
}
