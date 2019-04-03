@file:Suppress("DEPRECATION")

package vn.asiantech.travelmate.popularcityactivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_suggest.view.*
import vn.asiantech.travelmate.models.Travel

class CustomSearchViewAdapter(val context: Context, private val itemLayout: Int, var listPlace: ArrayList<Travel>, private val onClickListener: OnTouchItemDropdown) : BaseAdapter(), Filterable,
    View.OnTouchListener {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent?): Boolean {
        onClickListener.onTouchItem(v)
        return false
    }

    private var listResult : ArrayList<Travel> = listPlace
    override fun getCount(): Int {
        return listPlace.size
    }

    override fun getItem(index: Int): Travel? {
        return listPlace[index]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        view.tvSearch.text = getItem(position)?.name
        Glide.with(context).load(getItem(position)?.image).into(view.imgPlace)
        view?.setOnTouchListener(this)
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    listPlace = listResult
                    val searchStrLowerCase = constraint.toString().toLowerCase()
                    val matchValues = ArrayList<Travel>()
                    for (travel in listPlace) {
                        travel.name?.let {
                            if (it.toLowerCase().contains(searchStrLowerCase)) {
                                matchValues.add(travel)
                            }
                        }
                    }
                    filterResults.values = matchValues
                    filterResults.count = matchValues.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    if (results.values is ArrayList<*>) {
                        listPlace =
                            (results.values as ArrayList<*>).filterIsInstance<Travel>() as ArrayList<Travel>
                    }
                }
            }
        }
    }

    interface OnTouchItemDropdown{
        fun onTouchItem(view: View)
    }
}
