package vn.asiantech.travelmate.detailactivity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_placesearch.view.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.PlaceAutocomplete

class PlaceAutocompleteAdapter(
    private val listLocation: List<PlaceAutocomplete>,
    private val mListener: PlaceAutoCompleteInterface
) : RecyclerView.Adapter<PlaceAutocompleteAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceAutocompleteAdapter.PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_placesearch, parent, false)
        return PlaceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listLocation.size
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, p1: Int) {
        holder.onBind()
    }

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            val location = listLocation[adapterPosition]
            with(itemView) {
                tvName.text = location.description
                setOnClickListener { mListener.onItemclickListener(adapterPosition)
                }
            }
        }
    }

    interface PlaceAutoCompleteInterface {
        fun onItemclickListener(position: Int)
    }
}
