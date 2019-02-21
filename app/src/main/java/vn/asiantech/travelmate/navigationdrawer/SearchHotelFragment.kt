package vn.asiantech.travelmate.navigationdrawer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_search_hotel.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.Hotel

class SearchHotelFragment : Fragment(), AdapterView.OnItemClickListener, HotelAdapter.OnItemClickListener {
    override fun onClicked(position: Int) {

    }

    private var adapterHotel: HotelAdapter? = null
    private var listHotel: List<Hotel> = arrayListOf()
    private var suggestionAdapter: ArrayAdapter<String>? = null

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.w("xxxxxx", "position: ${actvSearchHotel.text.toString().trim()}")
        (listHotel as ArrayList).apply {
            clear()
            mockDataHotel()
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
            layoutManager = GridLayoutManager(context, 2)
            adapter = adapterHotel
        }
        Log.w("xxxxxx",2.5F.toString())
    }

    private fun mockData(): List<String> {
        val list = arrayListOf<String>()
        list.apply {
            add("Phuc")
            add("Dinh")
            add("Phu")
            add("Le")
            add("Luc")
        }
        return list
    }

    private fun mockDataHotel(): List<Hotel> {
        (listHotel as ArrayList).apply {
            add(Hotel("Galaxy", 2.5F, "Hue", 962908124L, "2520"))
            add(Hotel("MinhToan", 2.5F, "Danang", 962908124L, "2520"))
            add(Hotel("GreenHotel", 2.5F, "Hue", 962908124L, "2520"))
            add(Hotel("Novotel", 2.5F, "Hue", 962908124L, "2520"))
            add(Hotel("KingHotel", 2.5F, "Hue", 962908124L, "2520"))
        }
        return listHotel
    }
}