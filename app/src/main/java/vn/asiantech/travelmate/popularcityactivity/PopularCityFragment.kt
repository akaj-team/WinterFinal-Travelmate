package vn.asiantech.travelmate.popularcityactivity

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_popular_city.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.City

class PopularCityFragment : Fragment(), PopularCityAdapter.OnItemClickListener {
    private lateinit var database: DatabaseReference
    private var firebase: FirebaseDatabase? = FirebaseDatabase.getInstance()
    private var listCity: ArrayList<City> = arrayListOf()
    private lateinit var popularCityAdapter: PopularCityAdapter
    private var progressDialog: ProgressDialog? = null
    private lateinit var viewManager: GridLayoutManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popular_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initData()
    }

    private fun initData() {
        showProgressbarDialog()
        database = firebase!!.getReference("travel")
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                progressDialog?.dismiss()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (image in p0.children) {
                    val city = image.getValue(City::class.java)!!
                    listCity.add(city)
                }
                popularCityAdapter.notifyDataSetChanged()
                progressDialog?.dismiss()
            }
        })
    }

    private fun initRecyclerView() {
        viewManager = GridLayoutManager(context, 2)
        popularCityAdapter = PopularCityAdapter(listCity, this)
        recyclerViewPopularCity?.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = popularCityAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onClicked(position: Int) {
        Log.i("xxxxxx", listCity.get(position).name.toString())
    }
    private fun showProgressbarDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog?.setMessage(getString(R.string.note))
        progressDialog?.show()
    }
}
