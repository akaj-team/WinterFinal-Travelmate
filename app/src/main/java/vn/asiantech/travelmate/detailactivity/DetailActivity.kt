package vn.asiantech.travelmate.detailactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ProgressBar
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_detail.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.utils.Constant

class DetailActivity : AppCompatActivity() {
    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initFragment()
        initProgressBar()
        val name = intent.getStringExtra(Constant.DATA_FROM_POPULAR_ACTIVITY_TO_DETAIL_ACTIVITY)
        Log.w("xxxxxx",name)
    }

    private fun initProgressBar() {
        progressBar = ProgressBar(applicationContext, null, android.R.attr.progressBarStyleLarge)
        val params = RelativeLayout.LayoutParams(120, 120).apply {
            addRule(RelativeLayout.CENTER_IN_PARENT)
        }
        rlActivityDetail.addView(progressBar, params)
    }

    private fun initFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, DetailFragment())
            .commit()
    }
}
