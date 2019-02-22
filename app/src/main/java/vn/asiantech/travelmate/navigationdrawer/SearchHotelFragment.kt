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
import kotlinx.android.synthetic.main.fragment_search_hotel.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.Hotel

class SearchHotelFragment : Fragment(), AdapterView.OnItemClickListener, HotelAdapter.OnItemClickListener {

    private var adapterHotel: HotelAdapter? = null
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
            add(Hotel("Galaxy", 2.5F, "Hue", "0962908124", "2520", false))
            add(Hotel("MinhToan", 2.5F, "Danang", "0962908124", "2520", false))
            add(Hotel("GreenHotel", 2.5F, "Hue", "0962908124", "2520", false))
            add(Hotel("Novotel", 2.5F, "Hue", "0962908124", "2520", false))
            add(Hotel("KingHotel", 2.5F, "Hue", "0962908124", "2520", false))
            add(Hotel("KingHotel", 2.5F, "Hue", "0962908124", "2520", false))
        }
        return listHotel
    }

    override fun onLocationClicked(position: Int) {
        //Todo
    }

    override fun onMoreClicked(position: Int) {
        //Todo
    }

    override fun onCallClicked(position: Int) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse(getString(R.string.itemHotelTvMoreValues, listHotel[position].phoneNumber))
        startActivity(callIntent)
    }

    override fun onTapForMoreClicked(position: Int) {
        listHotel[position].isChecked = listHotel[position].isChecked != true
        adapterHotel?.notifyItemChanged(position)
    }
}
