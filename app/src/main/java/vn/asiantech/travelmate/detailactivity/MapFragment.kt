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
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.utils.Constant

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {
    companion object {
        const val REQUEST_CODE_ASK_PERMISSIONS_LOCATION = 123
    }

    private var mapFragment: SupportMapFragment? = null
    override fun onMyLocationClick(location: Location) {
        Toast.makeText(context, "Current location:\n$location", Toast.LENGTH_LONG).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(context, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        return false
    }

    private var mapGoogle: GoogleMap? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        checkAndRequestCameraPermission()
        initViews()
        return view
    }

    private fun initViews() {
        val fragmentManager = fragmentManager
        val supportMapFragment = SupportMapFragment.newInstance()
        fragmentManager?.beginTransaction()?.replace(R.id.flMap, supportMapFragment)?.commit()
        supportMapFragment.getMapAsync(this)
    }

    private fun checkAndRequestCameraPermission(): Boolean {
        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
            != PackageManager.PERMISSION_GRANTED) {
            activity?.let { temp ->
                ActivityCompat.requestPermissions(
                    temp, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_ASK_PERMISSIONS_LOCATION
                )
            }
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val fragmentManager = this.fragmentManager
                    mapFragment?.getMapAsync(this)

                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mapGoogle = googleMap
        val daNang = LatLng(16.078400, 108.234618)
        mapGoogle?.addMarker(MarkerOptions().position(daNang).title(Constant.MOCK_CITY))
        mapGoogle?.moveCamera(CameraUpdateFactory.newLatLng(daNang))
        mapGoogle?.uiSettings?.isZoomControlsEnabled = true
        mapGoogle?.uiSettings?.isCompassEnabled = true
        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
            == PackageManager.PERMISSION_GRANTED) {
            mapGoogle?.isMyLocationEnabled = true
        }
        mapGoogle?.setOnMyLocationButtonClickListener(this)
        mapGoogle?.setOnMyLocationClickListener(this)
    }
}
