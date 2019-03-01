package vn.asiantech.travelmate.popularcityactivity

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
    private var database: DatabaseReference ?= null
    private var firebase: FirebaseDatabase? = FirebaseDatabase.getInstance()
    private var listCity: ArrayList<Travel> = arrayListOf()
    private var popularCityAdapter: PopularCityAdapter? = null
    private val keyTravel = "travel"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popular_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initData()
    }

    private fun initData(){
        if (activity is PopularCityActivity) {
            (activity as PopularCityActivity).showProgressbarDialog()
            database = firebase?.getReference(Constant.KEY_TRAVEL)
            database?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    Toast.makeText(context, getString(R.string.checkInternet), Toast.LENGTH_SHORT).show()
                    (activity as PopularCityActivity).progressDialog?.dismiss()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (image in dataSnapshot.children) {
                        val city = image.getValue(Travel::class.java)
                        city?.let { listCity.add(it) }
                    }
                    popularCityAdapter?.notifyDataSetChanged()
                    (activity as? PopularCityActivity)?.progressDialog?.dismiss()
                }
            })
        }
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
        val travel = listCity.get(position)
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(keyTravel, travel)
        startActivity(intent)
    }
}
