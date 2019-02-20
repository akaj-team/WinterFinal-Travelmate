package vn.asiantech.travelmate.navigationdrawer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_suggestion.view.*
import vn.asiantech.travelmate.R

class SuggestionAdapter(private val listCity: ArrayList<String>, val onClickListener: OnItemClickListener) :
    RecyclerView.Adapter<SuggestionAdapter.SuggestViewHolder>() {

    class SuggestViewHolder(textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): SuggestionAdapter.SuggestViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(viewGroup.context)
        val view: TextView = layoutInflater.inflate(R.layout.item_suggestion, viewGroup, false) as TextView
        return SuggestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCity.size
    }

    override fun onBindViewHolder(viewHolder: SuggestViewHolder, position: Int) {
        viewHolder.itemView.tvSuggestion.text = listCity[position]
        viewHolder.itemView.setOnClickListener { onClickListener.onClicked(position) }
    }

    interface OnItemClickListener {
        fun onClicked(position: Int)
    }
}

