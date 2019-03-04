package vn.asiantech.travelmate.detailactivity

import android.content.pm.PackageManager
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.utils.Constant
import vn.asiantech.travelmate.models.Travel

class DetailActivity : AppCompatActivity() {
    var progressDialog: ProgressDialog? = null
    private var travel: Travel ?= null

    companion object {
        private const val KEY_TRAVEL: String = "travel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        travel = intent.getParcelableExtra(KEY_TRAVEL) as Travel
        progressDialog = ProgressDialog(this)
        initFragment()
    }

    private fun initFragment() {
        travel?.let { DetailFragment.newInstance(it) }?.let {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, it)
                .commit()
        }
    }

    fun showProgressbarDialog() {
        progressDialog?.apply {
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            setMessage(getString(R.string.note))
            show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Constant.REQUEST_CODE_ASK_PERMISSIONS_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as MapFragment
                    fragment.updateViewFragment()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
