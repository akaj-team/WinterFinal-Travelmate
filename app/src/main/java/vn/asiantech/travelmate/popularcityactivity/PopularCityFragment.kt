package vn.asiantech.travelmate.popularcityactivity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_popular_city.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.City

class PopularCityFragment : Fragment(), PopularCityAdapter.OnItemClickListener {
    private lateinit var popularCityAdapter: PopularCityAdapter
    private var listCity: ArrayList<City> = arrayListOf()
    private lateinit var viewManager: GridLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popular_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        viewManager = GridLayoutManager(context, 2)
        popularCityAdapter = PopularCityAdapter(mockData() as ArrayList<City>, this)
        recyclerViewPopularCity.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = popularCityAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onClicked(position: Int) {
        Log.d("xxxxxx", position.toString())
    }

    private fun mockData(): ArrayList<City>? {
        listCity.apply {
            add(
                City(
                    "Hue",
                    "http://webdulichhue.com/wp-content/uploads/dai-noi-hue-1-1024x666.jpg",
                    "ahiih",
                    "sadasd"
                )
            )
            add(City("Da Nang", "https://danangz.vn/wp-content/uploads/2016/12/phaohoa-1.jpg", "ahiih", "sadasd"))
            add(
                City(
                    "Ha Noi",
                    "https://evan.edu.vn/wp-content/uploads/2018/10/thuong_truong_thoi_tiet-10_18_10_725.jpg",
                    "ahiih",
                    "sadasd"
                )
            )
            add(City("Ho Chi Minh", "https://kenh14cdn.com/2017/3-1490678261405.jpeg", "ahiih", "sadasd"))
            add(
                City(
                    "Nha Trang",
                    "https://wiki-travel.com.vn/Uploads/picture/Viet_Dung-184602034655-nha-trang.jpg",
                    "ahiih",
                    "sadasd"
                )
            )
            add(
                City(
                    "Quang Binh",
                    "https://znews-photo.zadn.vn/w1024/Uploaded/mdf_eioxrd/2018_06_28/song_chay_1_zing.jpg",
                    "ahiih",
                    "sadasd"
                )
            )
            add(
                City(
                    "Vung Tau",
                    "http://www.galaxytourist.vn/uploads/post/img_1545884478203_o_1cvmuaank1c758jo1n2u1j0g1edfd.jpg",
                    "ahiih",
                    "sadasd"
                )
            )
            add(
                City(
                    "Sapa",
                    "https://revmaxtmc.com/images/upload/m/9/h/m9h_du-lich-sapa-thang-8-mytour-1.jpg",
                    "ahiih",
                    "sadasd"
                )
            )
        }
        return listCity
    }
}
