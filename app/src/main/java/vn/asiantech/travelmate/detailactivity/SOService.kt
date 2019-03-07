package vn.asiantech.travelmate.detailactivity

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import vn.asiantech.travelmate.models.WeatherList
import vn.asiantech.travelmate.models.WeatherResponse

interface SOService {
    @GET("weather")
    fun getCity(@Query("q") city: String, @Query("units") metric: String, @Query("appid") appId: String): Call<WeatherResponse>

    @GET("daily")
    fun getWeatherList(@Query("q") city: String, @Query("units") metric: String, @Query("cnt") cnt: Int, @Query("appid") appId: String): Call<WeatherList>
}