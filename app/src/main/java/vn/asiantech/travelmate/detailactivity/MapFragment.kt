package vn.asiantech.travelmate.detailactivity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.utils.Constant


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    private var supportMapFragment: SupportMapFragment? = null

    override fun onMyLocationClick(location: Location) {
        // TO DO
    }

    override fun onMyLocationButtonClick(): Boolean {
        //TO DO
        return false
    }

    private var mapGoogle: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        checkAndRequestPermission()
        initViews()
        return view
    }

    private fun initViews() {
        supportMapFragment = SupportMapFragment.newInstance()
        supportMapFragment?.let {
            fragmentManager?.beginTransaction()?.replace(R.id.flMap, it)?.commit()
            it.getMapAsync(this)
        }
    }

    private fun checkAndRequestPermission(): Boolean {
        if (context?.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { temp ->
                ActivityCompat.requestPermissions(
                    temp,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    Constant.REQUEST_CODE_ASK_PERMISSIONS_LOCATION
                )
            }
            return false
        }
        return true
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mapGoogle = googleMap
        val daNang = LatLng(16.078400, 108.234618)
        mapGoogle?.let {
            with(it) {
                addMarker(MarkerOptions().position(daNang).title(Constant.MOCK_CITY))
                    .setIcon(
                        BitmapDescriptorFactory.fromResource(
                            R.drawable.ic_building_24
                        )
                    )
                moveCamera(CameraUpdateFactory.newLatLng(daNang))
                uiSettings?.let { temp ->
                    temp.isZoomControlsEnabled = true
                    temp.isCompassEnabled = true
                }
                if (context?.let { temp ->
                        ContextCompat.checkSelfPermission(temp, Manifest.permission.ACCESS_FINE_LOCATION)
                    } == PackageManager.PERMISSION_GRANTED) {
                    isMyLocationEnabled = true
                }
                setOnMyLocationButtonClickListener(this@MapFragment)
                setOnMyLocationClickListener(this@MapFragment)
            }
        }
    }

    fun updateViewFragment() {
        supportMapFragment?.getMapAsync(this)
    }
}
