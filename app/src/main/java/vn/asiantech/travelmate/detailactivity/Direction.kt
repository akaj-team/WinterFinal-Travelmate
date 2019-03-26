package vn.asiantech.travelmate.detailactivity

import android.graphics.Color
import android.os.AsyncTask
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import vn.asiantech.travelmate.models.GoogleMapDTO
import vn.asiantech.travelmate.utils.APIUtil
import vn.asiantech.travelmate.utils.Constant

class Direction(
    private val myLocation: LatLng,
    private val travelLocation: LatLng,
    private val mapGoogle: GoogleMap
) : AsyncTask<Void, Void, List<List<LatLng>>>() {
    override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
        val result = ArrayList<List<LatLng>>()
        val path = ArrayList<LatLng>()
        val service = APIUtil.setUpApi(Constant.URL_API_MAP_GOOGLE)
        val googleMapDTO: GoogleMapDTO? = service.getDirection(
            false,
            Constant.URL_API_MAP_MODE,
            "${myLocation.latitude},${myLocation.longitude}",
            "${travelLocation.latitude},${travelLocation.longitude}"
            , Constant.API_MAP
        ).execute().body()
        googleMapDTO?.let { googleMap ->
            for (i in googleMap.routes[0].legs[0].steps.indices) {
                path.addAll(decodePolyline(googleMap.routes[0].legs[0].steps[i].polyline.points))
            }
            result.add(path)
        }
        return result
    }

    override fun onPostExecute(result: List<List<LatLng>>) {
        val lineOption = PolylineOptions()
        for (i in result.indices) {
            lineOption.apply {
                addAll(result[i])
                width(15f)
                color(Color.GRAY)
                geodesic(true)
            }
        }
        mapGoogle.addPolyline(lineOption)
    }
}

private fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
        poly.add(latLng)
    }
    return poly
}
