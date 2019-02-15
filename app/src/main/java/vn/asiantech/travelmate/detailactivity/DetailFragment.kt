package vn.asiantech.travelmate.detailactivity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
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
import vn.asiantech.travelmate.utils.Constant

class DetailFragment : Fragment(), View.OnClickListener {

    private var service: SOService? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setUpApi()
        getWeatherData(Constant.MOCK_CITY)
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        addListener(view)
        return view
    }

    private fun addListener(view: View?) {
        val cvWeather = view?.findViewById<CardView>(R.id.cvWeather)
        val imgMap = view?.findViewById<ImageView>(R.id.imgMap)
        cvWeather?.setOnClickListener(this)
        imgMap?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(
            R.anim.right_to_left1,
            R.anim.right_to_left2,
            R.anim.left_to_right1,
            R.anim.left_to_right2
        )
        when (v?.id) {
            R.id.cvWeather -> {
                fragmentTransaction?.replace(R.id.fragment_container, WeatherFragment())
            }
            R.id.imgMap -> {
                fragmentTransaction?.replace(R.id.fragment_container, MapFragment())
            }
        }
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
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
        service = getImagesRetrofit.create<SOService>(SOService::class.java)
    }

    private fun getWeatherData(city: String) {
        service?.getCity(city, Constant.UNITS, Constant.APP_ID)?.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>?) {
                val cityWeather: WeatherResponse? = response?.body()
                cityWeather?.let {
                    tvTemperature.text = getString(R.string.degreeC,it.tempDisplay)
                    tvHumidity.text = getString(R.string.percent,it.humidityDisplay)
                    tvWind.text = getString(R.string.meterOverSecond,it.speedDisplay)
                    context?.let { temp ->
                        Glide.with(temp).load(Constant.MOCK_IMAGE).into(imgCity)
                        Glide.with(temp).load("http://openweathermap.org/img/w/${it.iconDisplay}.png").into(imgIconWeather)
                    }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
