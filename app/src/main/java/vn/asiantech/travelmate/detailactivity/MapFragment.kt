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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.models.Travel
import vn.asiantech.travelmate.utils.Constant

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationChangeListener, View.OnClickListener {
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
                //Direction(myLocation, locate, map).execute()
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
        rlSearchFrom.setOnClickListener(this)
        rlSearchTo.setOnClickListener(this)
        initViews()
    }

    private fun initViews() {
        supportMapFragment = SupportMapFragment.newInstance()
        supportMapFragment?.let {
            fragmentManager?.beginTransaction()?.replace(R.id.flMap, it)?.commit()
            it.getMapAsync(this)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlSearchFrom -> {
                fragmentManager?.beginTransaction()?.setCustomAnimations(
                    R.anim.right_to_left1,
                    R.anim.right_to_left2,
                    R.anim.left_to_right1,
                    R.anim.left_to_right2
                )
                    ?.replace(R.id.fragment_container, SearchMapFragment.newInstance(1))
                    ?.addToBackStack("dfd")?.commit()
            }
            R.id.rlSearchTo -> {
                fragmentManager?.beginTransaction()?.setCustomAnimations(
                    R.anim.right_to_left1,
                    R.anim.right_to_left2,
                    R.anim.left_to_right1,
                    R.anim.left_to_right2
                )
                    ?.replace(R.id.fragment_container, SearchMapFragment.newInstance(2))
                    ?.addToBackStack("dfd")?.commit()
            }
        }
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
                } == PackageManager.PERMISSION_GRANTED) isMyLocationEnabled = true
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
            setMargins(0, 300, 180, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        val originPlace = (activity as DetailActivity).originPlace
        val destinationPlace = (activity as DetailActivity).destinationPlace
        tvName.text = originPlace.name
        tvNameTo.text = destinationPlace.name
        if (originPlace.position == 1 && destinationPlace.position == 2) {
            mapGoogle?.let { map ->
                Direction(
                    originPlace.latLng,
                    destinationPlace.latLng,
                    map
                ).execute()
            }
        }
    }
}
