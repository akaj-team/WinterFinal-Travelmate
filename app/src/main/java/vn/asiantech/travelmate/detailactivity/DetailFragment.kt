package vn.asiantech.travelmate.detailactivity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlin.math.ceil


class DetailFragment : Fragment(), View.OnClickListener {

    private var service: SOService? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpApi()
        weatherData(Constant.MOCK_CITY)
        cvWeather.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        fragmentManager?.beginTransaction()?.apply {
            setCustomAnimations(
                R.anim.right_to_left1,
                R.anim.right_to_left2,
                R.anim.left_to_right1,
                R.anim.left_to_right2
            )
            replace(R.id.fragment_container, WeatherFragment())
            addToBackStack(null)
            commit()
        }
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

    private fun weatherData(city: String) {
        service?.getCity(city, Constant.UNITS, Constant.APP_ID)?.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>?) {
                val cityWeather: WeatherResponse? = response?.body()
                tvTemperature.text = (cityWeather?.main?.temp?.let { ceil(it) }?.toInt().toString() + "°" + getString(R.string.metric))
                tvHumidity.text = (cityWeather?.main?.humidity?.let { ceil(it) }?.toInt().toString() + " " + getString(R.string.percent))
                tvWind.text = (cityWeather?.wind?.speed.toString() + " " + getString(R.string.meterOverSecond))
                context?.let {
                    Glide.with(it).load(Constant.MOCK_IMAGE).into(imgCity)
                    Glide.with(it).load("http://openweathermap.org/img/w/${cityWeather?.weather?.get(0)?.icon}.png")
                        .into(imgIconWeather)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
