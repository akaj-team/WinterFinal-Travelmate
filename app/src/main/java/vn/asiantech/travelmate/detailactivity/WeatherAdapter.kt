package vn.asiantech.travelmate.detailactivity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_weather.view.*
import kotlinx.android.synthetic.main.item_weather_top.view.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.extensions.toDateFormat
import vn.asiantech.travelmate.models.WeatherSevenDay
import vn.asiantech.travelmate.utils.Constant
import java.util.*

class WeatherAdapter(private var weatherItems: ArrayList<WeatherSevenDay>, var city: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ITEM_TOP = 0
        const val VIEW_TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(viewGroup.context)
        if (viewType == VIEW_TYPE_ITEM_TOP) {
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
            return VIEW_TYPE_ITEM_TOP
        }
        return VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ItemTopViewHolder) {
            viewHolder.onBind()
        } else if (viewHolder is ItemViewHolder) {
            viewHolder.onBind()
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            if (adapterPosition > 0) {
                val cityWeather = weatherItems[adapterPosition]
                with(itemView) {
                    with(cityWeather) {
                        Glide.with(context).load("${Constant.URL_ICON}${weather[0].icon}.png").into(imgIcon)
                        tvDayItem.text = dt.toDateFormat()
                        tvTemperatureItem.text = context.getString(R.string.degreeC, tempDisplay)
                    }
                }
            }
        }
    }

    inner class ItemTopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            if (adapterPosition == 0) {
                val cityWeather = weatherItems[0]
                with(itemView) {
                    with(cityWeather) {
                        tvDay.text = dt.toDateFormat()
                        tvCity.text = city
                        tvDescriptionWeather.text = weather[0].description
                        tvTemperature.text = context.getString(R.string.degreeC, tempDisplay)
                    }
                }
            }
        }
    }
}
