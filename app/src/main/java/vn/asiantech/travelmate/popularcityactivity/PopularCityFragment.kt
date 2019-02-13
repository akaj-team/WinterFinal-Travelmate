package vn.asiantech.travelmate.popularcityactivity

import android.content.Intent
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
import vn.asiantech.travelmate.detailactivity.DetailActivity
import vn.asiantech.travelmate.models.Travel

class PopularCityFragment : Fragment(), PopularCityAdapter.OnItemClickListener {
    private lateinit var database: DatabaseReference
    private var firebase: FirebaseDatabase? = FirebaseDatabase.getInstance()
    private lateinit var listCity: ArrayList<Travel>
    private lateinit var popularCityAdapter: PopularCityAdapter
    private var recyclerView: RecyclerView? = null
    private lateinit var viewManager: GridLayoutManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_popular_city, container, false)
        initData()
        return view
    }

    private fun initData() {
        listCity = ArrayList()
        database = firebase!!.getReference("travel")
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (image in p0.children) {
                    val city = image.getValue(Travel::class.java)!!
                    listCity.add(city)
                }
                initRecyclerView()
            }
        })
    }

    private fun initRecyclerView() {
        viewManager = GridLayoutManager(context, 2)
        popularCityAdapter = PopularCityAdapter(listCity, this)
        recyclerView = recyclerViewPopularCity?.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = popularCityAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onClicked(position: Int) {
        Log.i("xxxxxx", listCity.get(position).name.toString())
        val intent = Intent(activity, DetailActivity::class.java)
        startActivity(intent)
    }

}

