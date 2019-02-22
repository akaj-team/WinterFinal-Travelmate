package vn.asiantech.travelmate.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Travel(
    var area: String? = null,
    var description: String? = null,
    var image: String? = null,
    var location: String? = null,
    var name: String? = null,
    var province: String? = null
) : Parcelable
