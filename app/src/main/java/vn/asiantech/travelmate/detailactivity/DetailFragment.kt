package vn.asiantech.travelmate.detailactivity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_detail.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.WeatherResponse
import kotlin.math.ceil


class DetailFragment : Fragment(), View.OnClickListener {

    companion object {
        const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
        const val URL_LIST_SEVEN_DAYS = "http://api.openweathermap.org/data/2.5/forecast/"
        //const val APP_ID = "b1e590b8d7440070913a4c78bd976c84"
        const val APP_ID = "9de243494c0b295cca9337e1e96b00e2"
    }

    private var service: SOService? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setUpApi()
        weatherData("Danang")
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        addListener(view)
        return view
    }

    private fun addListener(view: View?) {
        val cvWeather = view?.findViewById<CardView>(R.id.cvWeather)
        cvWeather?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.add(R.id.fragment_container, WeatherFragment())
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }

    private fun setUpApi() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        val gson = GsonBuilder().setLenient().create()
        val getImagesRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
        service = getImagesRetrofit.create<SOService>(SOService::class.java)
    }

    private fun weatherData(city: String) {
        service?.getCity(city, "metric", APP_ID)?.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>?) {
                val cityWeather: WeatherResponse? = response?.body()

                tvTemperature.text =
                    (cityWeather?.main?.temp?.let { ceil(it) }?.toInt().toString() + " " + getString(R.string.metric))
                tvHumidity.text =
                    (cityWeather?.main?.humidity?.let { ceil(it) }?.toInt().toString() + " " + getString(R.string.percent))
                tvWind.text = (cityWeather?.wind?.speed.toString() + " " + getString(R.string.kilometersOverHours))
                context?.let {
                    Glide.with(it).load("https://danangz.vn/wp-content/uploads/2016/12/phaohoa-1.jpg").into(imgCity)
                }
                context?.let {
                    Glide.with(it).load("http://openweathermap.org/img/w/${cityWeather?.weather?.get(0)?.icon}.png")
                        .into(imgIconWeather)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
            }
        })
    }
}