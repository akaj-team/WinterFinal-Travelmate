package vn.asiantech.travelmate.detailactivity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import vn.asiantech.travelmate.R

class MapFragment : Fragment(), OnMapReadyCallback {
    var mapGoogle: GoogleMap? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        initViews()
        return view
    }

    private fun initViews() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mapGoogle = googleMap
        val daNang = LatLng(16.078400, 108.234618)
        mapGoogle?.addMarker(MarkerOptions().position(daNang).title("Danang"))
        mapGoogle?.moveCamera(CameraUpdateFactory.newLatLng(daNang))
    }
}
