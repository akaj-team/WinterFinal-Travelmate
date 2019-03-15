package vn.asiantech.travelmate.detailactivity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.location.places.AutocompletePredictionBuffer
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import kotlinx.android.synthetic.main.fragment_search.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.PlaceAutocomplete
import vn.asiantech.travelmate.models.PlaceMap
import java.util.concurrent.TimeUnit

class SearchMapFragment : Fragment(), PlaceAutocompleteAdapter.PlaceAutoCompleteInterface,
    GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener, Filterable {
    private var googleApiClient: GoogleApiClient? = null
    private var placeAutocompleteAdapter: PlaceAutocompleteAdapter? = null
    private var listPlace: ArrayList<PlaceAutocomplete> = arrayListOf()
    private var param: Int = 0

    companion object {
        const val POSITION = "position"
        fun newInstance(position: Int): SearchMapFragment {
            val fragment = SearchMapFragment()
            val args = Bundle()
            args.putInt(POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnected(p0: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgClear -> {
                edtSearch.setText("")
            }
            R.id.imgBack -> {
                hideKeyboard(activity as DetailActivity)
                activity?.onBackPressed()
            }
            else -> {
                //nothing
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
                val results = Filter.FilterResults()
                if (constraint != null) {
                    results.values = getAutocomplete(constraint)
                    results.count = getAutocomplete(constraint).size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: Filter.FilterResults?) {
                if (results != null && results.count > 0) {
                    if (results.values is List<*>) {
                        val list: List<PlaceAutocomplete> =
                            (results.values as List<*>).filterIsInstance<PlaceAutocomplete>()
                        listPlace.clear()
                        listPlace.addAll(list)
                    }
                    placeAutocompleteAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onItemclickListener(position: Int) {
        try {
            val placeId = listPlace[position].placeId
            var placeResult: PendingResult<PlaceBuffer>? = null
            googleApiClient?.let {
                placeResult = Places.GeoDataApi
                    .getPlaceById(it, placeId)
            }
            placeResult?.setResultCallback { places ->
                if (places.count == 1) {
                    val place = places[0]
                    Toast.makeText(context, places[0].name.toString(), Toast.LENGTH_SHORT).show()
                    if (param == 1) {
                        (activity as DetailActivity).originPlace = PlaceMap(1, place.name.toString(), place.latLng)
                    } else {
                        (activity as DetailActivity).destinationPlace = PlaceMap(2, place.name.toString(), place.latLng)
                    }
                    hideKeyboard(activity as DetailActivity)
                    activity?.onBackPressed()
                } else {
                    Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {

        }

    }

    override fun onStart() {
        googleApiClient?.connect()
        super.onStart()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            googleApiClient = GoogleApiClient.Builder(this.context!!)
                .enableAutoManage(it, 0, this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build()
        }
        arguments?.let {
            param = it.getInt(POSITION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        placeAutocompleteAdapter = PlaceAutocompleteAdapter(listPlace, this)
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtSearch.requestFocus()
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        recycleView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = placeAutocompleteAdapter
        }
        imgClear.setOnClickListener(this)
        imgBack.setOnClickListener(this)
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                filter.filter(s.toString())
                if (edtSearch.text.isEmpty()) {
                    imgClear.visibility = View.GONE
                    listPlace.clear()
                } else {
                    imgClear.visibility = View.VISIBLE
                }
                placeAutocompleteAdapter?.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

    }

    override fun onPause() {
        super.onPause()
        activity?.let {
            googleApiClient?.stopAutoManage(it)
            googleApiClient?.disconnect()
        }
    }

    override fun onStop() {
        googleApiClient?.disconnect()
        super.onStop()
    }

    fun getAutocomplete(constraint: CharSequence?): ArrayList<PlaceAutocomplete> {
        var resultList: ArrayList<PlaceAutocomplete> = arrayListOf()
        googleApiClient?.let {
            if (it.isConnected) {
                val results = Places.GeoDataApi
                    .getAutocompletePredictions(
                        it, constraint.toString(),
                        null, null
                    )
                val autocompletePredictions: AutocompletePredictionBuffer = results
                    .await(60, TimeUnit.SECONDS)
                val iterator = autocompletePredictions.iterator()
                resultList = ArrayList(autocompletePredictions.count)
                while (iterator.hasNext()) {
                    val prediction = iterator.next()
                    resultList.add(
                        PlaceAutocomplete(
                            prediction.placeId.toString(),
                            prediction.getPrimaryText(null).toString()
                        )
                    )
                }
                autocompletePredictions.release()
            }
        }
        return resultList
    }

    fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
