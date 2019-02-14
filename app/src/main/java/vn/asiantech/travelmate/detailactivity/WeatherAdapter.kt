package vn.asiantech.travelmate.detailactivity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_weather.view.*
import kotlinx.android.synthetic.main.item_weather_top.view.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.WeatherSevenDay
import vn.asiantech.travelmate.utils.Constant
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class WeatherAdapter(private var weatherItems: ArrayList<WeatherSevenDay>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val viewTypeItemTop = 0
        const val viewTypeItem = 1
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(viewGroup.context)
        if (viewType == viewTypeItemTop) {
            val view: View = layoutInflater.inflate(R.layout.item_weather_top, viewGroup, false)
            return ItemTopViewHolder(view)
        }
        val view: View = layoutInflater.inflate(R.layout.item_weather, viewGroup, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherItems.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return viewTypeItemTop
        }
        return viewTypeItem
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ItemTopViewHolder) {
            viewHolder.onBind()
        } else if (viewHolder is ItemViewHolder) {
            viewHolder.onBind()
        }
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ItemViewHolder(itemView: View) : ViewHolder(itemView) {
        fun onBind() {
            val cityWeather = weatherItems[adapterPosition]
            if (adapterPosition > 0) {
                itemView.tvTemperatureItem.text = (cityWeather.temp.day.let {
                    ceil(it).toInt().toString()
                } + itemView.context.getString(R.string.detailFragmentTvTemperatureItemDegree) + itemView.context.getString(R.string.metric))
                itemView.tvDayItem.text = formatDate(cityWeather.dt)
                itemView.context?.let {
                    Glide.with(it).load("http://openweathermap.org/img/w/${cityWeather.weather[0].icon}.png")
                        .into(itemView.imgIcon)
                }
            }
        }
    }

    inner class ItemTopViewHolder(itemView: View) : ViewHolder(itemView) {
        fun onBind() {
            val cityWeather = weatherItems[0]
            itemView.tvCity.text = Constant.MOCK_CITY
            itemView.tvDescriptionWeather.text = cityWeather.weather[0].description
            itemView.tvTemperature.text =
                (cityWeather.temp.day.let {
                    ceil(it).toInt().toString()
                } + itemView.context.getString(R.string.detailFragmentTvTemperatureItemDegree) + itemView.context.getString(R.string.metric))
            itemView.tvDay.text = formatDate(cityWeather.dt)
        }
    }

    fun formatDate(day: Int): String? {
        val dayFormat = day.toLong()
        val date: Date? = Date(dayFormat.times(1000L))
        val simpleDateFormat = SimpleDateFormat(Constant.SIMPLE_DAY_FORMAT, Locale.US)
        return simpleDateFormat.format(date)
    }
}
