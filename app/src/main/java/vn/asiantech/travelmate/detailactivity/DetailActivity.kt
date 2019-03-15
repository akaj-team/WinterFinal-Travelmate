package vn.asiantech.travelmate.detailactivity

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.PlaceMap
import vn.asiantech.travelmate.models.Travel
import vn.asiantech.travelmate.utils.Constant

class DetailActivity : AppCompatActivity() {
    var originPlace = PlaceMap(0, "Your location", LatLng(1.0, 1.0))
    var destinationPlace = PlaceMap(0, "Choose destination...", LatLng(1.0, 1.0))
    private var progressDialog: ProgressDialog? = null
    private var travel: Travel? = null

    companion object {
        private const val KEY_TRAVEL: String = "travel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        checkAndRequestPermission()
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

    private fun checkAndRequestPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Constant.REQUEST_CODE_ASK_PERMISSIONS_LOCATION
            )
            return false
        }
        return true
    }

    fun showProgressbarDialog() {
        progressDialog?.apply {
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            setMessage(getString(R.string.note))
            show()
        }
    }

    fun dismissProgressbarDialog() {
        progressDialog?.dismiss()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Constant.REQUEST_CODE_ASK_PERMISSIONS_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.w("xxxxx", "ok")
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
