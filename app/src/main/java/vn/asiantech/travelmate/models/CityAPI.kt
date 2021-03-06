package vn.asiantech.travelmate.models

import com.google.gson.annotations.SerializedName
import kotlin.math.ceil

data class WeatherResponse(

    @SerializedName("coord")
    var coord: Coord,

    @SerializedName("sys")
    var sys: Sys,

    @SerializedName("weather")
    var weather: List<Weather>,

    @SerializedName("main")
    var main: Main,

    @SerializedName("wind")
    var wind: Wind,

    @SerializedName("rain")
    var rain: Rain,

    @SerializedName("clouds")
    var clouds: Clouds,

    @SerializedName("dt")
    var dt: Float,

    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("cod")
    var cod: Float
) {
    val tempDisplay: Int
        get() = ceil(main.temp).toInt()
    val humidityDisplay: Int
        get() = main.humidity.toInt()
    val speedDisplay: Int
        get() = wind.speed.toInt()
    val iconDisplay: String
        get() = weather[0].icon
}

data class Weather(
    @SerializedName("id")
    var id: Int,

    @SerializedName("main")
    var main: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("icon")
    var icon: String
)

data class Clouds(
    @SerializedName("all")
    var all: Float
)

data class Rain(
    @SerializedName("3h")
    var h3: Float
)

data class Wind(
    @SerializedName("speed")
    var speed: Float,

    @SerializedName("deg")
    var deg: Float
)

data class Main(
    @SerializedName("temp")
    var temp: Float,

    @SerializedName("humidity")
    var humidity: Float,

    @SerializedName("pressure")
    var pressure: Float,

    @SerializedName("temp_min")
    var tempMin: Float,

    @SerializedName("temp_max")
    var tempMax: Float
)

data class Sys(
    @SerializedName("country")
    var country: String,

    @SerializedName("sunrise")
    var sunrise: Long,

    @SerializedName("sunset")
    var sunset: Long
)

data class Coord(
    @SerializedName("lon")
    var lon: Float,

    @SerializedName("lat")
    var lat: Float
)
