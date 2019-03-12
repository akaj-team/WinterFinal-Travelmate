@file:Suppress("DEPRECATION")

package vn.asiantech.travelmate.navigationdrawer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_search_hotel.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.extensions.hideKeyboard
import vn.asiantech.travelmate.models.Hotel
import vn.asiantech.travelmate.models.Travel
import vn.asiantech.travelmate.popularcityactivity.PopularCityActivity
import vn.asiantech.travelmate.utils.Constant

class SearchHotelFragment : Fragment(), AdapterView.OnItemClickListener, HotelAdapter.OnItemClickListener {

    private var database: DatabaseReference? = null
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
            addAll(getDataHotels(listCity[idCity].province.toString()))
        }
        view?.hideKeyboard()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_hotel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        context?.let { suggestionAdapter = ArrayAdapter(it, R.layout.item_suggestion, getPlaces()) }
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

    private fun getPlaces(): List<String> {
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

    private fun getDataHotels(place: String): MutableList<Hotel> {
        (activity as? PopularCityActivity)?.showProgressbarDialog()
        getHotelByProvince = firebase?.getReference(Constant.KEY_HOTEL)?.orderByChild(Constant.KEY_PROVINCE)?.equalTo(place)
        getHotelByProvince?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                (activity as? PopularCityActivity)?.dismissProgressbarDialog()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                (activity as? PopularCityActivity)?.dismissProgressbarDialog()
                for (hotel in dataSnapshot.children) {
                    hotel.getValue(Hotel::class.java)?.let {
                        listHotel.add(it)
                    }
                }
                adapterHotel?.notifyDataSetChanged()
            }
        })
        return listHotel
    }

    override fun onLocationClicked(position: Int) {
    }

    override fun onMoreClicked(position: Int) {
    }

    override fun onCallClicked(position: Int) {
        if (checkPermissionForCall()){
            Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse(getString(R.string.itemHotelTvMoreValues, listHotel[position].phone))
                startActivity(this)
            }
        }
    }

    override fun onTapForMoreClicked(position: Int) {
        listHotel[position].isChecked = listHotel[position].isChecked != true
        adapterHotel?.notifyItemChanged(position)
    }

    private fun checkPermissionForCall(): Boolean {
        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CALL_PHONE) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.CALL_PHONE), Constant.REQUEST_ASK_PERMISSION_CALL) }
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
}
