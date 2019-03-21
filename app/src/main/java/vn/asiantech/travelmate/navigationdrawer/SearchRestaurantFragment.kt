package vn.asiantech.travelmate.navigationdrawer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_search_restaurant.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.Hotel

class SearchRestaurantFragment : Fragment(), AdapterView.OnItemClickListener, RestaurantAdapter.OnItemClickListener {

    private var adapterHotel: RestaurantAdapter? = null
    private var listHotel: MutableList<Hotel> = mutableListOf()
    private var suggestionAdapter: ArrayAdapter<String>? = null

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        listHotel.apply {
            clear()
            addAll(mockDataHotel())
        }
        adapterHotel?.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        context?.let { suggestionAdapter = ArrayAdapter(it, R.layout.item_suggestion, mockData()) }
        actvSearchRestaurant.apply {
            setAdapter(suggestionAdapter)
            threshold = 1
            onItemClickListener = this@SearchRestaurantFragment
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    private fun initRecyclerView() {
        adapterHotel = RestaurantAdapter((listHotel as ArrayList<Hotel>), this)
        recyclerViewRestaurant.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterHotel
        }
    }

    private fun mockData(): List<String> {
        val listCity = mutableListOf<String>()
        listCity.apply {
            add("Hue")
            add("Da Nang")
            add("Quang Nam")
            add("Quang Ngai")
            add("Binh Dinh")
        }
        return listCity
    }

    private fun mockDataHotel(): MutableList<Hotel> {
        val listHotel = mutableListOf<Hotel>()
        listHotel.apply {
            add(Hotel("Zero nine", 2.5F, "Hue", "0962908124", "2520", false))
            add(Hotel("Banh Canh", 2.5F, "Danang", "0962908124", "2520", false))
            add(Hotel("Bun Bo Hue", 2.5F, "Hue", "0962908124", "2520", false))
            add(Hotel("Mi Tom", 2.5F, "Hue", "0962908124", "2520", false))
            add(Hotel("Mi Quang", 2.5F, "Hue", "0962908124", "2520", false))
            add(Hotel("Com Tam", 2.5F, "Hue", "0962908124", "2520", false))
        }
        return listHotel
    }

    override fun onLocationClicked(position: Int) {
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.frameLayoutDrawer, RestaurantDetailFragment.newInstance(position))
            commit()
        }
    }
}
