package vn.asiantech.travelmate.popularcityactivity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_suggestion.view.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.Travel

class SuggestionAdapter(private var listData: ArrayList<Travel>, private val context: Context) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int,convertView: View, parent: ViewGroup?): View {
        convertView = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.item_suggestion,
                parent,
                false
            )
        val item: Travel = getItem(position)
        convertView.tvSuggestion.text = item.name
        return convertView
    }

    override fun getItem(position: Int): Travel {
        return listData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listData.size
    }

//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                constraint?.let {
//                    with(FilterResults()) {
//                        with(listData) {
//                            values = this@SuggestionAdapter.listData
//                            count = size
//                        }
//                    }
//                }
//                return FilterResults()
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                if (results != null && results.count > 0) {
//                    listData = results.values as ArrayList<Travel>
//                    notifyDataSetChanged()
//                } else {
//                    notifyDataSetInvalidated()
//                }
//            }
//
//        }
//    }
}