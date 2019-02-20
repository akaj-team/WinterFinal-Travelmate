package vn.asiantech.travelmate.popularcityactivity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_popular_city.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.Travel
import vn.asiantech.travelmate.detailactivity.DetailActivity
import vn.asiantech.travelmate.utils.Constant

class PopularCityFragment : Fragment(), PopularCityAdapter.OnItemClickListener {
    private lateinit var database: DatabaseReference
    private var firebase: FirebaseDatabase? = FirebaseDatabase.getInstance()
    private var listCity: ArrayList<Travel> = arrayListOf()
    private var progressDialog: ProgressDialog? = null
    private lateinit var viewManager: GridLayoutManager
    private lateinit var popularCityAdapter: PopularCityAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popular_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initData()
    }

    private fun initData(){
        showProgressbarDialog()
        database = firebase!!.getReference(Constant.KEY_TRAVEL)
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, getString(R.string.checkInternet), Toast.LENGTH_SHORT).show()
                progressDialog?.dismiss()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (image in p0.children) {
                    val city = image.getValue(Travel::class.java)!!
                    listCity.add(city)
                }
                popularCityAdapter.notifyDataSetChanged()
                progressDialog?.dismiss()
            }
        })
    }

    private fun initRecyclerView() {
        popularCityAdapter = PopularCityAdapter(listCity, this)
        recyclerViewPopularCity?.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = popularCityAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onClicked(position: Int) {
//        val travel = listCity.get(position)
        val intent = Intent(activity,DetailActivity::class.java)
        startActivity(intent)
    }
    private fun showProgressbarDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog?.setMessage(getString(R.string.note))
        progressDialog?.show()
    }
}
