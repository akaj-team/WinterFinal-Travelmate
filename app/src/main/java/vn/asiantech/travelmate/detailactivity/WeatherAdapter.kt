package vn.asiantech.travelmate.detailactivity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_weather.view.*
import kotlinx.android.synthetic.main.item_weather_top.view.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.WeatherSevenDay

class WeatherAdapter(private var weatherItems: ArrayList<WeatherSevenDay>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val viewTypeItemTop = 0
    private val viewTypeItem = 1
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

    open inner class ItemViewHolder(itemView: View) : ViewHolder(itemView) {
        fun onBind() {
            val cityWeather = weatherItems[adapterPosition]
            if (adapterPosition > 0) {
                itemView.tvTemperatureItem.text = cityWeather.temp?.day.toString()
                itemView.tvDayItem.text = cityWeather.dt.toString()
            }
        }
    }

    inner class ItemTopViewHolder(itemView: View) : ViewHolder(itemView) {
        fun onBind() {
            val cityWeather = weatherItems[0]
            //itemView.tvCity.text = cityWeather?.city?.name
            itemView.tvTemperature.text = cityWeather.temp?.day.toString()
            itemView.tvDay.text = cityWeather.dt.toString()
        }
    }
}
