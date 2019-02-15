package vn.asiantech.travelmate.detailactivity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_weather.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.WeatherList
import vn.asiantech.travelmate.models.WeatherSevenDay
import vn.asiantech.travelmate.utils.Constant
import vn.asiantech.travelmate.utils.SetAPIUtil

class WeatherFragment : Fragment() {
    private lateinit var weatherAdapter: WeatherAdapter
    private var weatherItems: ArrayList<WeatherSevenDay> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getWeatherData(Constant.MOCK_CITY)
    }

    private fun initView() {
        val viewManager = LinearLayoutManager(context)
        weatherAdapter = WeatherAdapter(weatherItems)
        recyclerViewWeather.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = weatherAdapter
        }
    }

    private fun getWeatherData(city: String) {
        val setAPIUtil = SetAPIUtil()
        val service = setAPIUtil.setUpApi(Constant.URL_LIST_SEVEN_DAYS)
        service.getWeatherList(city, Constant.UNITS, 7, Constant.APP_ID)
            ?.enqueue(object : Callback<WeatherList> {
                override fun onResponse(call: Call<WeatherList>, response: Response<WeatherList>?) {
                    response?.body()?.list?.let { weatherItems.addAll(it) }
                    weatherAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<WeatherList>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}
