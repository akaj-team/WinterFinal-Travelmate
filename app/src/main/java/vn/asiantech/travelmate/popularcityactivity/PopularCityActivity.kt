package vn.asiantech.travelmate.popularcityactivity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_popular_city.*
import vn.asiantech.travelmate.R

class PopularCityActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_city)
        initDrawer()
        initView()
        initFragment()
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
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navDestination -> {
                // Handle the camera action
            }
            R.id.navHotel -> {

            }
            R.id.navLogout -> {

            }
            R.id.navSetting -> {

            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initView() {
        val actionBar = supportActionBar
        actionBar?.title = "TravelMate"
        val drawer= ContextCompat.getDrawable(applicationContext,R.drawable.ic_search_white_24dp)
    }

    override fun onClick(v: View?) {
        drawerLayout.openDrawer(Gravity.START)
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutDrawer, PopularCityFragment())
            commit()
        }
    }
}
