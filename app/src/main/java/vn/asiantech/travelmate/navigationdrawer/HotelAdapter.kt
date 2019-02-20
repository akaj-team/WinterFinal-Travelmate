package vn.asiantech.travelmate.navigationdrawer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.item_hotel.view.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.Hotel

class HotelAdapter(private val listHotel: ArrayList<Hotel>, val onClickListener: OnItemClickListener) :
    RecyclerView.Adapter<HotelAdapter.SuggestViewHolder>() {

    inner class SuggestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            val hotel: Hotel = listHotel[adapterPosition]
            with(itemView) {
                with(hotel) {
                    tvDistance.text = distance.toString()
                    tvHotelName.text = hotelName
                    tvAddress.text = address
                    tvMoreDetail.setOnClickListener { view ->
                        Toast.makeText(context,"Position: $adapterPosition", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): HotelAdapter.SuggestViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(viewGroup.context)
        val view: View = layoutInflater.inflate(R.layout.item_hotel, viewGroup, false)
        return SuggestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listHotel.size
    }

    override fun onBindViewHolder(viewHolder: SuggestViewHolder, position: Int) {
        viewHolder.onBind()
    }

    interface OnItemClickListener {
        fun onClicked(position: Int)
    }
}


