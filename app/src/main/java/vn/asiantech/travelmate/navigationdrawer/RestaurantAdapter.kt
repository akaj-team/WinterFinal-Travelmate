package vn.asiantech.travelmate.navigationdrawer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_restaurant.view.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.Hotel


class RestaurantAdapter(private val listHotel: ArrayList<Hotel>, val onClickListener: OnItemClickListener) :
    RecyclerView.Adapter<RestaurantAdapter.SuggestViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): RestaurantAdapter.SuggestViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_restaurant, viewGroup, false)
        return SuggestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listHotel.size
    }

    override fun onBindViewHolder(viewHolder: SuggestViewHolder, position: Int) {
        viewHolder.onBind()
    }

    inner class SuggestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            val hotel: Hotel = listHotel[adapterPosition]
            with(itemView) {
                with(hotel) {
                    tvRestaurantName.text = hotelName
                    ratingBar.rating = 2F
                    setOnClickListener {
                        onClickListener.onLocationClicked(adapterPosition)
                    }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onLocationClicked(position: Int)
    }
}
