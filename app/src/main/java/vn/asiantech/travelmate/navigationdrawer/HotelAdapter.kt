package vn.asiantech.travelmate.navigationdrawer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_hotel.view.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.Hotel

class HotelAdapter(private val listHotel: ArrayList<Hotel>, val onClickListener: OnItemClickListener) :
    RecyclerView.Adapter<HotelAdapter.SuggestViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): HotelAdapter.SuggestViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_hotel, viewGroup, false)
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
                    tvDistance.text = context.getString(R.string.itemHotelKilometer, distance)
                    tvHotelName.text = hotelName
                    tvAddress.text = address
                    tvTapForMoreDetail.setOnClickListener {
                        onClickListener.onTapForMoreClicked(adapterPosition)
                    }
                    itemView.isSelected = isChecked
                    if (isChecked) {
                        llMoreDetail.visibility = View.VISIBLE
                    } else {
                        llMoreDetail.visibility = View.GONE
                    }
                    imgCall.setOnClickListener { onClickListener.onCallClicked(adapterPosition) }
                    imgLocation.setOnClickListener { onClickListener.onLocationClicked(adapterPosition) }
                    tvMoreDetail.setOnClickListener { onClickListener.onMoreClicked(adapterPosition) }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onCallClicked(position: Int)
        fun onLocationClicked(position: Int)
        fun onMoreClicked(position: Int)
        fun onTapForMoreClicked(position: Int)
    }
}


