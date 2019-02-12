package vn.asiantech.travelmate.popularcityactivity

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

import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.City

class PopularCityFragment : Fragment(), PopularCityAdapter.OnItemClickListener {
    private lateinit var database : DatabaseReference
    private var firebase : FirebaseDatabase? = FirebaseDatabase.getInstance()
    private lateinit var listCity: ArrayList<City>
    private lateinit var popularCityAdapter: PopularCityAdapter
    private var recyclerView: RecyclerView? = null
    private lateinit var viewManager: GridLayoutManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_popular_city, container, false)
        initRecyclerView(view)
        return view
    }

    private fun initRecyclerView(view: View) {
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
                Log.i("aaa", listCity.size.toString())
                recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPopularCity)?.apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = popularCityAdapter
                    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
                    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                }
            }
        })
        /*viewManager = GridLayoutManager(context, 2)
        popularCityAdapter = PopularCityAdapter(listCity, this)
        Log.i("aaa", listCity.size.toString())
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPopularCity)?.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = popularCityAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }*/
    }

    override fun onClicked(position: Int) {
        //move to detail activity
    }

    private fun mockData(): List<City> {
        /*listCity.add(
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
        )*/
        return listCity
    }

}
