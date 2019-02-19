package vn.asiantech.travelmate.popularcityactivity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_popular_city.*
import vn.asiantech.travelmate.R

class PopularCityActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,
    SearchView.OnQueryTextListener, SearchView.OnSuggestionListener, SuggestionAdapter.OnItemClickListener {

    private var listData: List<String> = arrayListOf()
    private var adapter: ArrayAdapter<String>? = null
    override fun onClicked(position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_city)
        initDrawer()
        initView()
        initFragment()
        mockData()
        adapter = ArrayAdapter(this, R.layout.item_suggestion, listData)
        lvSuggest.adapter = adapter
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
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.actionSearch).actionView as SearchView
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(this@PopularCityActivity)
            setOnSuggestionListener(this@PopularCityActivity)
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun mockData(): List<String> {
        (listData as ArrayList<String>).apply {
            add("Apple")
            add("Banana")
            add("Pineapple")
            add("Orange")
            add("Gavava")
            add("Peech")
            add("Melon")
            add("Watermelon")
            add("Papaya")
        }
        return listData
    }

    //------Search----------
    override fun onQueryTextSubmit(query: String): Boolean {
        Log.w("xxxx----", query)
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        adapter?.filter?.filter(newText)
        return false
    }

    override fun onSuggestionSelect(p0: Int): Boolean {

        return true
    }

    override fun onSuggestionClick(p0: Int): Boolean {

        return true
    }

    //---------------------------------

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionSearch -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navDestination -> {

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
}
