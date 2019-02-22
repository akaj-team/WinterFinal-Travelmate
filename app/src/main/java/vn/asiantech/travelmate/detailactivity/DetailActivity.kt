package vn.asiantech.travelmate.detailactivity

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.Travel
import vn.asiantech.travelmate.utils.Constant

class DetailActivity : AppCompatActivity() {
    lateinit var travel: Travel
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        progressDialog = ProgressDialog(this)
        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, DetailFragment())
            .commit()
    }

    fun getCity(): Travel {
        travel = intent.getParcelableExtra(Constant.keyTravel) as Travel
        return travel
    }

    fun showProgressbarDialog() {
        progressDialog?.apply {
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            setMessage(getString(R.string.note))
            show()
        }
    }
}
