package vn.asiantech.travelmate.detailactivity

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.Travel

class DetailActivity : AppCompatActivity() {
    var progressDialog: ProgressDialog? = null
    private var travel: Travel ?= null
    private val keyTravel: String = "travel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        travel = intent.getParcelableExtra(keyTravel) as Travel
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
}
