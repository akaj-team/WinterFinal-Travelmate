package vn.asiantech.travelmate.popularcityactivity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.popular_city_item.view.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.City

class PopularCityAdapter(private val listCity: ArrayList<City>, private val onItemclickListener: OnItemClickListener) :
    RecyclerView.Adapter<PopularCityAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularCityAdapter.ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.popular_city_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount() = listCity.size
    inner class ImageViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        override fun onClick(v: View?) {
            onItemclickListener.onClicked(adapterPosition)
        }

        fun onBind() {
            val city = listCity[adapterPosition]
            Glide.with(itemView.context).load(city.image).into(itemView.imgCity)
            view.tvCityName.text = city.name
            itemView.setOnClickListener(this)
        }
    }

    interface OnItemClickListener {
        fun onClicked(position: Int)
    }
}
