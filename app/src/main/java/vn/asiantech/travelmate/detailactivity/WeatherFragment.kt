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
import vn.asiantech.travelmate.models.Travel
import vn.asiantech.travelmate.models.WeatherList
import vn.asiantech.travelmate.models.WeatherSevenDay
import vn.asiantech.travelmate.utils.APIUtil
import vn.asiantech.travelmate.utils.Constant

class WeatherFragment : Fragment() {
    private lateinit var travel: Travel
    private lateinit var weatherAdapter: WeatherAdapter
    private var weatherItems: ArrayList<WeatherSevenDay> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity is DetailActivity) {
            travel = (activity as DetailActivity).getCity()
        }
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getWeatherData(travel.province.toString())
    }

    private fun initView() {
        weatherAdapter = WeatherAdapter(weatherItems, travel.province.toString())
        recyclerViewWeather.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = weatherAdapter
        }
    }

    private fun getWeatherData(city: String) {
        if (activity is DetailActivity) {
            (activity as DetailActivity).showProgressbarDialog()
            val service = APIUtil.setUpApi(Constant.URL_LIST_SEVEN_DAYS)
            service.getWeatherList(city, Constant.UNITS, 7, Constant.APP_ID)
                ?.enqueue(object : Callback<WeatherList> {
                    override fun onResponse(call: Call<WeatherList>, response: Response<WeatherList>?) {
                        response?.body()?.list?.let { weatherItems.addAll(it) }
                        weatherAdapter.notifyDataSetChanged()
                        (activity as DetailActivity).progressDialog?.dismiss()
                    }

                    override fun onFailure(call: Call<WeatherList>, t: Throwable) {
                        (activity as DetailActivity).progressDialog?.dismiss()
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
