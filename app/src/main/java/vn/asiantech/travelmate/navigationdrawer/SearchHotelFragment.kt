package vn.asiantech.travelmate.navigationdrawer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_search_hotel.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.extensions.getInputText


class SearchHotelFragment : Fragment() {
    private var suggestionAdapter: SuggestionAdapter? = null
    private var listCity: List<String> = arrayListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_hotel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initListView()
        //edtSearchHotel.addTextChangedListener(this)
    }

//    override fun afterTextChanged(s: Editable?) {
//        (listCity as ArrayList).apply {
//            clear()
//            addAll(mockData())
//        }
//        val text = edtSearchHotel.getInputText()
//
//        if (text.isNotEmpty()) {
//            mockData().forEach { temp ->
//                if (!temp.toUpperCase().contains(text.toUpperCase())) {
//                    (listCity as ArrayList).remove(temp)
//                }
//                Log.w("xxxxx", temp)
//
//            }
//        }else{
//            (listCity as ArrayList).clear()
//        }
//        suggestionAdapter?.notifyDataSetChanged()
//
//    }
//
//    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//    }
//
//    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//    }
//
//    private fun initListView() {
//        suggestionAdapter = SuggestionAdapter(listCity as ArrayList<String>,this)
//        recyclerViewSuggest.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(context)
//            adapter = suggestionAdapter
//        }
//    }

    private fun mockData(): List<String> {
        val list = arrayListOf<String>()
        list.apply {
            add("Phuc")
            add("Dinh")
            add("Phu")
            add("Le")
            add("Luc")
        }
        return list
    }
}