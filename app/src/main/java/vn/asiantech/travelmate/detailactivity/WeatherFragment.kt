package vn.asiantech.travelmate.detailactivity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_weather.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.WeatherList
import vn.asiantech.travelmate.models.WeatherSevenDay
import vn.asiantech.travelmate.utils.Constant

class WeatherFragment : Fragment() {
    private var service: SOService? = null
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var viewManager: LinearLayoutManager
    private var weatherItems: ArrayList<WeatherSevenDay> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpApi()
        initView()
        getWeatherData(Constant.MOCK_CITY)
    }

    private fun initView() {
        viewManager = LinearLayoutManager(context)
        weatherAdapter = WeatherAdapter(weatherItems)
        recyclerViewWeather.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = weatherAdapter
        }
    }

    private fun setUpApi() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        val gson = GsonBuilder().setLenient().create()
        val getImagesRetrofit = Retrofit.Builder()
            .baseUrl(Constant.URL_LIST_SEVEN_DAYS)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
        service = getImagesRetrofit.create<SOService>(SOService::class.java)
    }

    private fun getWeatherData(city: String) {
        service?.getWeatherList(city, Constant.UNITS, 7, Constant.APP_ID)
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
