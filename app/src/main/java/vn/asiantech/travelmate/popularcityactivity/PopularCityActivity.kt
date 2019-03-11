package vn.asiantech.travelmate.popularcityactivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.view.inputmethod.InputMethodManager
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
import vn.asiantech.travelmate.models.Travel
import vn.asiantech.travelmate.navigationdrawer.SearchHotelFragment
import vn.asiantech.travelmate.navigationdrawer.SettingFragment
import vn.asiantech.travelmate.utils.Constant
import vn.asiantech.travelmate.models.User
import vn.asiantech.travelmate.utils.ValidationUtil
import java.util.*

class PopularCityActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {
    private var database: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var firebase: FirebaseDatabase? = FirebaseDatabase.getInstance()
    private var listCity: ArrayList<Travel> = arrayListOf()
    private var listPlace: MutableList<String> = mutableListOf()
    private var fireBaseUser: FirebaseUser? = firebaseAuth?.currentUser
    private var progressDialog: ProgressDialog? = null
    private var user: User? = null
    private var suggestionAdapter: ArrayAdapter<String>? = null
    private var autoCompleteTextView: AutoCompleteTextView? = null
    private var fragment: SettingFragment ?= null

    companion object {
        private const val KEY_TRAVEL = "travel"
        private const val KEY_SAVE_VALUE = "value"
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        hideKeyBoard()
        Handler().postDelayed({
            val idCity = listPlace.indexOf(parent?.getItemAtPosition(position))
            Intent(this, DetailActivity::class.java).apply {
                putExtra(KEY_TRAVEL, listCity[idCity])
                startActivity(this)
            }
        }, 500)

    }

    fun hideKeyBoard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_city)
        progressDialog = ProgressDialog(this)
        initDrawer()
        initView()
        initFragment()
        fragment = SettingFragment()
        getInforUser()
    }

    private fun initSuggestion() {
        applicationContext?.let { suggestionAdapter = ArrayAdapter(it, R.layout.item_suggestion, getPlaces()) }
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
                navView.post {
                    user = path?.let { ValidationUtil.getValuePathChild(it) }?.let { dataSnapshot.child(it).getValue(User::class.java) }
                    user?.let {
                        with(it) {
                            Glide.with(this@PopularCityActivity).load(avatar).into(imgAvatar)
                            tvName.text = lastName
                        }
                    }
                }
            }
        })
    }

    private fun initDrawer() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose)
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
            hint = getString(R.string.search)
        }
        initSuggestion()
        return super.onCreateOptionsMenu(menu)
    }

    private fun getPlaces(): MutableList<String> {
        database = firebase?.getReference(Constant.KEY_TRAVEL)
        database?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (image in dataSnapshot.children) {
                    image.getValue(Travel::class.java)?.let {
                        listCity.add(it)
                    }
                    image.child(Constant.KEY_NAME).value?.let {
                        listPlace.add(it.toString())
                    }
                }
            }
        })
        return listPlace
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
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutDrawer, PopularCityFragment())
                        .commit()
                }
            }
            R.id.navHotel -> {
                if (fragment is PopularCityFragment) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutDrawer, SearchHotelFragment())
                        .addToBackStack(null)
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutDrawer, SearchHotelFragment())
                        .commit()
                }
            }
            R.id.navLogout -> {
                firebaseAuth?.signOut()
                getSharedPreferences(Constant.FILE_NAME, Context.MODE_PRIVATE).apply {
                    edit().apply {
                        putBoolean(KEY_SAVE_VALUE, false)
                        apply()
                    }
                }
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.navSetting ->
                when (fragment) {
                    is PopularCityFragment -> {
                        user?.let { SettingFragment.newInstance(it) }?.let {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.frameLayoutDrawer, it)
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                    is SettingFragment -> {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    else -> {
                        user?.let { SettingFragment.newInstance(it) }?.let {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.frameLayoutDrawer, it)
                                .commit()
                        }
                    }
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

    fun dismissProgressbarDialog(){
        progressDialog?.dismiss()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val isShowRationaleWrite: Boolean
        when (requestCode) {
            Constant.REQUEST_ASK_PERMISSION_CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    fragment?.chooseCamera()
                } else {
                    val isShowRationaleCamera =
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                    isShowRationaleWrite = ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    if (!isShowRationaleWrite || !isShowRationaleCamera) {
                        showSettingsAlert(getString(R.string.noteCamera))
                    }
                }
            }
            Constant.REQUEST_ASK_PERMISSION_GALLERY -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fragment?.chooseGallery()
                } else {
                    isShowRationaleWrite = ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    if (!isShowRationaleWrite) {
                        showSettingsAlert(getString(R.string.noteGallery))
                    }
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun showSettingsAlert(message: String) {
        AlertDialog.Builder(this).create().apply {
            setTitle(getString(R.string.optionChoose))
            setMessage(getString(R.string.noteAccess) + " " + message)
            setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.setting)) { dialog, _ ->
                dialog.dismiss()
                startInstalledAppDetailsActivity(this@PopularCityActivity)
            }
            show()
        }
    }

    private fun startInstalledAppDetailsActivity(context: Activity?) {
        context?.let {
            Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:" + context.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                context.startActivity(this)
            }
        }
    }
}
