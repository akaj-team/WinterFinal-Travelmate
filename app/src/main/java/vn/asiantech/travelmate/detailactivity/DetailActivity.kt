package vn.asiantech.travelmate.detailactivity

import android.content.pm.PackageManager
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MapFragment.REQUEST_CODE_ASK_PERMISSIONS_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as MapFragment
                    fragment.updateViewFragment()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
