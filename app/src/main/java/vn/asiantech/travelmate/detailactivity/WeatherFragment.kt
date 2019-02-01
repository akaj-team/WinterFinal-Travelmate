package vn.asiantech.travelmate.detailactivity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.GsonBuilder
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

class WeatherFragment : Fragment(){
    private var service: SOService? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var weatherItems: ArrayList<WeatherSevenDay>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        weatherItems = ArrayList()
        setUpApi()
        weatherData(Constant.MOCK_CITY)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        viewManager = LinearLayoutManager(view.context)
        weatherAdapter = WeatherAdapter(weatherItems)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewWeather).apply {
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
            .baseUrl(DetailFragment.URL_LIST_SEVEN_DAYS)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
        service = getImagesRetrofit.create<SOService>(SOService::class.java)
    }

    private fun weatherData(city: String) {
        service?.getWeatherList(city, DetailFragment.UNITS, 7, DetailFragment.APP_ID)
            ?.enqueue(object : Callback<WeatherList> {
                override fun onResponse(call: Call<WeatherList>, response: Response<WeatherList>?) {
                    weatherItems.addAll(response?.body()?.list!!)
                    weatherAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<WeatherList>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}
