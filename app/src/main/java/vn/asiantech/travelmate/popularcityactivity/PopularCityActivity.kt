package vn.asiantech.travelmate.popularcityactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.City

class PopularCityActivity : AppCompatActivity(),PopularCityAdapter.OnItemClickListener {
    private lateinit var listCity: ArrayList<City>
    private lateinit var popularCityAdapter: PopularCityAdapter
    private var recyclerView: RecyclerView? = null
    private lateinit var viewManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_city)
       initRecyclerView()
    }

    private fun initRecyclerView() {
        listCity = ArrayList()
        viewManager = GridLayoutManager(applicationContext, 2)
        popularCityAdapter = PopularCityAdapter(mockData() as ArrayList<City>, this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewPopularCity)?.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = popularCityAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onClicked(position: Int) {
        //val intent = Intent(this,)
    }

    private fun mockData(): List<City> {
        listCity.add(
            City(
                "Hue",
                "http://webdulichhue.com/wp-content/uploads/dai-noi-hue-1-1024x666.jpg",
                "ahiih",
                "sadasd"
            )
        )
        listCity.add(City("Da Nang", "https://danangz.vn/wp-content/uploads/2016/12/phaohoa-1.jpg", "ahiih", "sadasd"))
        listCity.add(
            City(
                "Ha Noi",
                "https://evan.edu.vn/wp-content/uploads/2018/10/thuong_truong_thoi_tiet-10_18_10_725.jpg",
                "ahiih",
                "sadasd"
            )
        )
        listCity.add(City("Ho Chi Minh", "https://kenh14cdn.com/2017/3-1490678261405.jpeg", "ahiih", "sadasd"))
        listCity.add(
            City(
                "Nha Trang",
                "https://wiki-travel.com.vn/Uploads/picture/Viet_Dung-184602034655-nha-trang.jpg",
                "ahiih",
                "sadasd"
            )
        )
        listCity.add(
            City(
                "Quang Binh",
                "https://znews-photo.zadn.vn/w1024/Uploaded/mdf_eioxrd/2018_06_28/song_chay_1_zing.jpg",
                "ahiih",
                "sadasd"
            )
        )
        listCity.add(
            City(
                "Vung Tau",
                "http://www.galaxytourist.vn/uploads/post/img_1545884478203_o_1cvmuaank1c758jo1n2u1j0g1edfd.jpg",
                "ahiih",
                "sadasd"
            )
        )
        listCity.add(
            City(
                "Sapa",
                "https://revmaxtmc.com/images/upload/m/9/h/m9h_du-lich-sapa-thang-8-mytour-1.jpg",
                "ahiih",
                "sadasd"
            )
        )
        return listCity
    }
}
