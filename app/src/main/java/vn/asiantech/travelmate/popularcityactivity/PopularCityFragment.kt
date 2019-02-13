package vn.asiantech.travelmate.popularcityactivity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_popular_city.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.City

class PopularCityFragment : Fragment() {
    private lateinit var database : DatabaseReference
    private var firebase : FirebaseDatabase? = FirebaseDatabase.getInstance()
    private lateinit var listCity: ArrayList<City>
    private lateinit var popularCityAdapter: PopularCityAdapter
    private var recyclerView: RecyclerView? = null
    private lateinit var viewManager: GridLayoutManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_popular_city, container, false)
        initRecyclerView()
        return view
    }

    private fun initRecyclerView() {
        listCity = ArrayList()
        database = firebase!!.getReference("travel")
        database.addValueEventListener(object : ValueEventListener, PopularCityAdapter.OnItemClickListener {
            override fun onClicked(position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (image in p0.children) {
                    val city = image.getValue(City::class.java)!!
                    listCity.add(city)
                }
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
        })
    }
}
