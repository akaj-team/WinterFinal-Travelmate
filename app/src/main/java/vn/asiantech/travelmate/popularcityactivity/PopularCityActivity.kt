package vn.asiantech.travelmate.popularcityactivity

import android.app.ProgressDialog
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.activity_popular_city.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.login.LoginActivity
import vn.asiantech.travelmate.navigationdrawer.SearchHotelFragment
import vn.asiantech.travelmate.navigationdrawer.SettingFragment
import vn.asiantech.travelmate.utils.Constant
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.nav_header_main.view.*


class PopularCityActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_city)
        initDrawer()
        initHeader()
        initView()
        initFragment()
    }

    private fun initHeader() {
        val navigationView = findViewById(R.id.navView) as NavigationView
        val imgAvatar = navigationView.getHeaderView(0).imgAvatar
        Glide.with(applicationContext).load(Constant.URL_AVATAR).into(imgAvatar)
    }

    private fun initDrawer() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionSearch -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.frameLayoutDrawer)
        when (item.itemId) {
            R.id.navDestination -> {
                if (fragment is PopularCityFragment) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    val intent = Intent(this, PopularCityActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.navHotel -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutDrawer, SearchHotelFragment())
                    .commit()
            }
            R.id.navLogout -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.navSetting -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutDrawer, SettingFragment())
                    .commit()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initView() {
        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.populatFragmentTravelMate)
        supportActionBar?.title = getString(R.string.travelMate)
    }

    override fun onClick(v: View?) {
        drawerLayout.openDrawer(Gravity.START)
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutDrawer, PopularCityFragment())
            .commit()
    }

    fun getPassLogin(): String {
        intent.getStringExtra(Constant.KEY_PASSWORD)?.let {
            val password = intent.getStringExtra(Constant.KEY_PASSWORD)
            return password
        }
        return ""
    }

    fun showProgressbarDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog?.setMessage(getString(R.string.note))
        progressDialog?.show()
    }
}
