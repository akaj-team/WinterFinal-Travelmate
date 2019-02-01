package vn.asiantech.travelmate.models

import com.google.gson.annotations.SerializedName

data class WeatherList(

    @SerializedName("city")
    var city: City,

    @SerializedName("cod")
    var cod: String,

    @SerializedName("message")

    var message: Double,

    @SerializedName("cnt")
    var cnt: Int,

    @SerializedName("list")
    var list: List<WeatherSevenDay>
)

data class City(

    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("coord")
    var coord: Coord,

    @SerializedName("country")
    var country: String,

    @SerializedName("population")
    var population: Int
)

data class WeatherSevenDay(

    @SerializedName("dt")
    var dt: Int,

    @SerializedName("temp")
    var temp: Temp,

    @SerializedName("pressure")
    var pressure: Double,

    @SerializedName("humidity")
    var humidity: Int,

    @SerializedName("weather")
    var weather: List<WeatherCity>,

    @SerializedName("speed")
    var speed: Double,

    @SerializedName("deg")
    var deg: Int,

    @SerializedName("clouds")
    var clouds: Int,

    @SerializedName("rain")
    var rain: Double
)

data class Temp(

    @SerializedName("day")
    var day: Double,

    @SerializedName("min")
    var min: Double,

    @SerializedName("max")
    var max: Double,

    @SerializedName("night")
    var night: Double,

    @SerializedName("eve")
    var eve: Double,

    @SerializedName("morn")
    var morn: Double
)

data class WeatherCity(

    @SerializedName("id")
    var id: Int,

    @SerializedName("main")
    var main: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("icon")
    var icon: String
)
