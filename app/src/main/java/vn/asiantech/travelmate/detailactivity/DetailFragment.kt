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
import vn.asiantech.travelmate.models.Travel
import vn.asiantech.travelmate.models.WeatherResponse
import vn.asiantech.travelmate.utils.APIUtil
import vn.asiantech.travelmate.utils.Constant


class DetailFragment : Fragment(), View.OnClickListener {

    private var travel: Travel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity is DetailActivity) {
            travel = (activity as DetailActivity).getCity()
        }
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWeatherData(travel?.province.toString())
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

    private fun getWeatherData(city: String) {
        if (activity is DetailActivity) {
            (activity as DetailActivity).showProgressbarDialog()
            val service = APIUtil.setUpApi(Constant.BASE_URL)
            service.getCity(city, Constant.UNITS, Constant.APP_ID)?.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>?) {
                    val cityWeather: WeatherResponse? = response?.body()
                    cityWeather?.let {
                        with(it) {
                            tvTemperature.text = getString(R.string.degreeC, tempDisplay)
                            tvHumidity.text = getString(R.string.percent, humidityDisplay)
                            tvWind.text = getString(R.string.meterOverSecond, speedDisplay)
                            tvDescription.text = travel?.description
                            tvNamePlace.text = travel?.name
                            Glide.with(this@DetailFragment).load(travel?.image).into(imgCity)
                            Glide.with(this@DetailFragment).load("${Constant.URL_ICON}$iconDisplay.png")
                                .into(imgIconWeather)
                        }
                    }
                    (activity as DetailActivity).progressDialog?.dismiss()
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    (activity as DetailActivity).progressDialog?.dismiss()
                }
            })
        }
    }
}
