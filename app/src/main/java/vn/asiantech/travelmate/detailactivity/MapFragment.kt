package vn.asiantech.travelmate.detailactivity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.Travel
import vn.asiantech.travelmate.utils.Constant
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationChangeListener {
    private var supportMapFragment: SupportMapFragment? = null
    private var travel: Travel? = null
    private var mapGoogle: GoogleMap? = null
    private var locationTravel: LatLng? = null

    companion object {
        fun newInstance(travel: Travel) = MapFragment().apply {
            arguments = Bundle().apply {
                putParcelable(WeatherFragment.KEY_TRAVEL, travel)
            }
        }
    }

    override fun onMyLocationClick(location: Location) {
        //TO DO
    }

    override fun onMyLocationButtonClick(): Boolean {
        //TO DO
        return false
    }

    override fun onMyLocationChange(location: Location) {
        mapGoogle?.let { map ->
            val myLocation = LatLng(location.latitude, location.longitude)
            locationTravel?.let { locate ->
                getUrl(myLocation, locate)
            }?.let {
                GetDirection(it, map).execute()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            travel = arguments?.getParcelable(WeatherFragment.KEY_TRAVEL)
        }
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        supportMapFragment = SupportMapFragment.newInstance()
        supportMapFragment?.let {
            fragmentManager?.beginTransaction()?.replace(R.id.flMap, it)?.commit()
            it.getMapAsync(this)
        }
        context?.let {
            Places.initialize(it, Constant.API_MAP).toString()
        }
        activity?.let { Places.createClient(it) }
        val autoCompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment_from) as AutocompleteSupportFragment
        autoCompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        autoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val placeLocate = place.latLng
                mapGoogle?.run {
                    clear()
                    addMarker(placeLocate?.let { temp -> MarkerOptions().position(temp).title(place.name) })
                    addMarker(locationTravel?.let { temp -> MarkerOptions().position(temp).title(travel?.name) })
                        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_travel))
                    moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocate, Constant.MAP_ZOOM))
                    uiSettings?.let { temp ->
                        temp.isZoomControlsEnabled = true
                    }
                }
            }

            override fun onError(status: Status) {
                Toast.makeText(context, status.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mapGoogle = googleMap
        locationTravel = travel?.latitude?.let { lat -> travel?.longitude?.let { long -> LatLng(lat, long) } }
        mapGoogle?.run {
            addMarker(locationTravel?.let { temp -> MarkerOptions().position(temp).title(travel?.name) })
                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_travel))
            moveCamera(CameraUpdateFactory.newLatLngZoom(locationTravel, Constant.MAP_ZOOM))
            uiSettings?.let { temp ->
                temp.isZoomControlsEnabled = true
                temp.isCompassEnabled = true
            }
            if (context?.let { temp ->
                    ContextCompat.checkSelfPermission(temp, Manifest.permission.ACCESS_FINE_LOCATION)
                } == PackageManager.PERMISSION_GRANTED) {
                isMyLocationEnabled = true
            }
            customMyLocation()
            setOnMyLocationButtonClickListener(this@MapFragment)
            setOnMyLocationClickListener(this@MapFragment)
            setOnMyLocationChangeListener(this@MapFragment)
        }
    }

    private fun customMyLocation() {
        (supportMapFragment?.view?.findViewById<View>("2".toInt())?.layoutParams as RelativeLayout.LayoutParams).apply {
            addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
            setMargins(0, 180, 180, 0)
        }
    }

    private fun getUrl(myLocation: LatLng, travelLocation: LatLng): String {
        return "${Constant.URL_API_MAP}origin=${myLocation.latitude},${myLocation.longitude}&destination=${travelLocation.latitude},${travelLocation.longitude}"
    }
}
