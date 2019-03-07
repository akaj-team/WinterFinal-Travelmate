package vn.asiantech.travelmate.navigationdrawer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_search_hotel.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.Hotel
import vn.asiantech.travelmate.models.Travel
import vn.asiantech.travelmate.popularcityactivity.PopularCityActivity
import vn.asiantech.travelmate.utils.Constant

class SearchHotelFragment : Fragment(), AdapterView.OnItemClickListener, HotelAdapter.OnItemClickListener {

    private var adapterHotel: HotelAdapter? = null
    private var listHotel: MutableList<Hotel> = mutableListOf()
    private var suggestionAdapter: ArrayAdapter<String>? = null
    private var listCity: ArrayList<Travel> = arrayListOf()
    private var listPlace: MutableList<String> = mutableListOf()
    private var firebase: FirebaseDatabase? = FirebaseDatabase.getInstance()
    private var getHotelByProvince :Query ?= null

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val idCity = listPlace.indexOf(parent?.getItemAtPosition(position))
        listHotel.apply {
            clear()
            addAll(mockDataHotel(listCity.get(idCity).province.toString()))
        }
        adapterHotel?.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_hotel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        context?.let { suggestionAdapter = ArrayAdapter(it, R.layout.item_suggestion, mockData()) }
        actvSearchHotel.apply {
            setAdapter(suggestionAdapter)
            threshold = 1
            onItemClickListener = this@SearchHotelFragment
        }
    }

    private fun initRecyclerView() {
        adapterHotel = HotelAdapter((listHotel as ArrayList<Hotel>), this)
        recyclerViewHotel.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = adapterHotel
        }
    }

    private fun mockData(): List<String> {
        (activity as? PopularCityActivity)?.getData(listCity, listPlace)
        return listPlace
    }

    private fun mockDataHotel(place: String): MutableList<Hotel> {
        getHotelByProvince = firebase?.getReference(Constant.KEY_HOTEL)?.orderByChild(Constant.KEY_PROVINCE)?.equalTo(place)
        getDataHotel()
        return listHotel
    }

    private fun getDataHotel() {
        getHotelByProvince?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (hotel in dataSnapshot.children) {
                    hotel.getValue(Hotel::class.java)?.let {
                        listHotel.add(it)
                    }
                }
            }
        })
    }

    override fun onLocationClicked(position: Int) {
    }

    override fun onMoreClicked(position: Int) {
    }

    override fun onCallClicked(position: Int) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse(getString(R.string.itemHotelTvMoreValues, listHotel[position].phone))
        startActivity(callIntent)
    }

    override fun onTapForMoreClicked(position: Int) {
        listHotel[position].isChecked = listHotel[position].isChecked != true
        adapterHotel?.notifyItemChanged(position)
    }
}
