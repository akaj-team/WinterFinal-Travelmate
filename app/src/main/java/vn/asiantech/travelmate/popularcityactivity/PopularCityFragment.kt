package vn.asiantech.travelmate.popularcityactivity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_popular_city.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.detailactivity.DetailActivity
import vn.asiantech.travelmate.models.Travel
import vn.asiantech.travelmate.utils.Constant
import java.util.*

class PopularCityFragment : Fragment(), PopularCityAdapter.OnItemClickListener {
    private var firebase: FirebaseDatabase? = FirebaseDatabase.getInstance()
    private var listCity: ArrayList<Travel> = arrayListOf()
    private var popularCityAdapter: PopularCityAdapter? = null

    companion object {
        private const val KEY_TRAVEL = "travel"
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popular_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initData()
    }

    private fun initData(){
        (activity as? PopularCityActivity)?.showProgressbarDialog()
        val query: Query? = firebase?.getReference(Constant.KEY_TRAVEL)?.limitToFirst(Constant.NUMBER_ITEM)
        query?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Toast.makeText(context, getString(R.string.checkInternet), Toast.LENGTH_SHORT).show()
                (activity as? PopularCityActivity)?.dismissProgressbarDialog()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (image in dataSnapshot.children) {
                    image.getValue(Travel::class.java)?.let {
                        listCity.add(it)
                    }
                }
                popularCityAdapter?.notifyDataSetChanged()
                (activity as? PopularCityActivity)?.dismissProgressbarDialog()
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
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(KEY_TRAVEL, listCity.get(position))
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).getSupportActionBar()?.show()
    }
}
