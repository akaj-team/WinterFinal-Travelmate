package vn.asiantech.travelmate.detailactivity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.WeatherResponse
import vn.asiantech.travelmate.utils.APIUtil
import vn.asiantech.travelmate.utils.Constant

class DetailFragment : Fragment(), View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWeatherData(Constant.MOCK_CITY)
        cvWeather.setOnClickListener(this)
        imgMap.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        fragmentManager?.beginTransaction()?.apply {
            setCustomAnimations(
                R.anim.right_to_left1,
                R.anim.right_to_left2,
                R.anim.left_to_right1,
                R.anim.left_to_right2
            )
            when (v?.id) {
                R.id.cvWeather -> {
                    replace(R.id.fragment_container, WeatherFragment())
                }
                R.id.imgMap -> {
                    replace(R.id.fragment_container, MapFragment())
                }
            }
            addToBackStack(null)
            commit()
        }
    }

    private fun getWeatherData(city: String) {
        if (activity is DetailActivity) {
            (activity as DetailActivity).progressBar?.visibility = View.VISIBLE
            val service = APIUtil.setUpApi(Constant.BASE_URL)
            service.getCity(city, Constant.UNITS, Constant.APP_ID)?.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>?) {
                    val cityWeather: WeatherResponse? = response?.body()
                    cityWeather?.let {
                        with(it) {
                            tvTemperature.text = getString(R.string.degreeC, tempDisplay)
                            tvHumidity.text = getString(R.string.percent, humidityDisplay)
                            tvWind.text = getString(R.string.meterOverSecond, speedDisplay)
                            Glide.with(this@DetailFragment).load(Constant.MOCK_IMAGE).into(imgCity)
                            Glide.with(this@DetailFragment).load("${Constant.URL_ICON}$iconDisplay.png")
                                .into(imgIconWeather)
                        }
                    }
                    (activity as DetailActivity).progressBar?.visibility = View.GONE
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    (activity as DetailActivity).progressBar?.visibility = View.GONE
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
