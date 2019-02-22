package vn.asiantech.travelmate.popularcityactivity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_popular_city.*
import kotlinx.android.synthetic.main.nav_header_main.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.detailactivity.DetailActivity
import vn.asiantech.travelmate.login.LoginActivity
import vn.asiantech.travelmate.models.User
import vn.asiantech.travelmate.navigationdrawer.SearchHotelFragment
import vn.asiantech.travelmate.navigationdrawer.SettingFragment
import vn.asiantech.travelmate.utils.Constant
import vn.asiantech.travelmate.utils.ValidationUtil

class PopularCityActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,
    AdapterView.OnItemClickListener {

    private var database: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var fireBaseUser: FirebaseUser? = firebaseAuth?.currentUser
    var progressDialog: ProgressDialog? = null
    var user: User? = null
    private var listData: MutableList<String> = mutableListOf()
    private var suggestionAdapter: ArrayAdapter<String>? = null
    private var autoCompleteTextView: AutoCompleteTextView? = null

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(Constant.DATA_FROM_POPULAR_ACTIVITY_TO_DETAIL_ACTIVITY, Constant.MOCK_CITY)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_city)
        progressDialog = ProgressDialog(this)
        getInforUser()
        initDrawer()
        initView()
        initFragment()
    }

    private fun initSuggestion() {
        applicationContext?.let { suggestionAdapter = ArrayAdapter(it, R.layout.item_suggestion, mockData()) }
        autoCompleteTextView?.apply {
            setAdapter(suggestionAdapter)
            threshold = 1
            width = maxWidth
            onItemClickListener = this@PopularCityActivity
        }
    }

    private fun getInforUser() {
        val path = fireBaseUser?.email
        database = FirebaseDatabase.getInstance().getReference(Constant.KEY_ACCOUNT)
        database?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = path?.let { ValidationUtil.getValuePathChild(it) }
                    ?.let { dataSnapshot.child(it).getValue(User::class.java) }
                user?.let {
                    with(it) {
                        Glide.with(this@PopularCityActivity).load(avatar).into(imgAvatar)
                        tvName.text = lastName
                    }
                }
            }
        })
    }

    private fun initDrawer() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigationDrawerOpen,
            R.string.navigationDrawerClose
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
        autoCompleteTextView = menu.findItem(R.id.actionSearch).actionView as AutoCompleteTextView
        autoCompleteTextView?.apply {
            maxLines = 1
            hint = "  Search ..."
        }
        initSuggestion()
        return super.onCreateOptionsMenu(menu)
    }

    private fun mockData(): MutableList<String> {
        listData.apply {
            add("Dana")
            add("Hue")
            add("Quang Binh")
            add("Quang Tri")
            add("Quang Nam")
            add("Quang Ngai")
            add("Ha Noi")
            add("Dak lak")
            add("Dong Nai")
        }
        return listData
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
                if (fragment is SearchHotelFragment) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutDrawer, SearchHotelFragment())
                        .commit()
                }
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

    fun showProgressbarDialog() {
        progressDialog?.apply {
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            setMessage(getString(R.string.note))
            show()
        }
    }
}
