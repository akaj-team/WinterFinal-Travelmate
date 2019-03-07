package vn.asiantech.travelmate.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Travel(
    var area: String? = null,
    var description: String? = null,
    var image: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var name: String? = null,
    var province: String? = null
) : Parcelable
