package vn.asiantech.travelmate.detailactivity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vn.asiantech.travelmate.models.WeatherList;
import vn.asiantech.travelmate.models.WeatherResponse;

public interface SOService {
    @GET("weather")
    Call<WeatherResponse> getCity(@Query("q") String city, @Query("units") String metric, @Query("appid") String appId);

    @GET("daily")
    Call<WeatherList> getWeatherList(@Query("q") String city, @Query("units") String metric, @Query("cnt") int cnt, @Query("appid") String appId);
}
