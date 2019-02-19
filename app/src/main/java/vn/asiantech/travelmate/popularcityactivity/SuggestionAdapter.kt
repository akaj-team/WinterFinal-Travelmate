package vn.asiantech.travelmate.popularcityactivity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_suggestion.view.*
import vn.asiantech.travelmate.R

class SuggestionAdapter(private val listSuggestion: ArrayList<String>, private val onItemclickListener: OnItemClickListener) :
    RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SuggestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_suggestion, parent, false)
        return SuggestionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listSuggestion.size
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, p1: Int) {
        holder.onBind()
    }

    inner class SuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            val nameCity = listSuggestion[adapterPosition]
            with(itemView) {
                itemView.tvSuggestion.text = nameCity
                setOnClickListener { onItemclickListener.onClicked(adapterPosition) }
            }
        }

    }

    interface OnItemClickListener {
        fun onClicked(position: Int)
    }

}