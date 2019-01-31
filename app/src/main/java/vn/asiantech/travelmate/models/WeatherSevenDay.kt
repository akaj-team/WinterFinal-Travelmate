package vn.asiantech.travelmate.models

import com.google.gson.annotations.SerializedName


class WeatherList {

    @SerializedName("city")
    var city: City? = null

    @SerializedName("cod")
    var cod: String = ""

    @SerializedName("message")

    var message: Double = 0.0

    @SerializedName("cnt")
    var cnt: Int = 0

    @SerializedName("list")
    var list: List<WeatherSevenDay>? = null
}


class City {

    @SerializedName("id")
    var id: Int? = 0
    @SerializedName("name")

    var name: String = ""

    @SerializedName("coord")
    var coord: Coord? = null

    @SerializedName("country")

    var country: String = ""

    @SerializedName("population")
    var population: Int? = 0
}

class WeatherSevenDay {
    @SerializedName("dt")
    var dt: Int = 0

    @SerializedName("temp")
    var temp: Temp? = null

    @SerializedName("pressure")
    var pressure: Double = 0.0

    @SerializedName("humidity")
    var humidity: Int = 0

    @SerializedName("weather")
    var weather: List<WeatherCity>? = null

    @SerializedName("speed")
    var speed: Double = 0.0

    @SerializedName("deg")
    var deg: Int = 0

    @SerializedName("clouds")
    var clouds: Int = 0

    @SerializedName("rain")

    var rain: Double = 0.0
}

class Temp {

    @SerializedName("day")
    var day: Double = 0.0

    @SerializedName("min")

    var min: Double = 0.0

    @SerializedName("max")

    var max: Double = 0.0

    @SerializedName("night")

    var night: Double = 0.0

    @SerializedName("eve")

    var eve: Double = 0.0

    @SerializedName("morn")
    var morn: Double = 0.0
}

class WeatherCity {

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("main")
    var main: String = ""

    @SerializedName("description")
    var description: String = ""

    @SerializedName("icon")
    var icon: String = ""
}
