package vn.asiantech.travelmate.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hotel(
    var name: String ?= null,
    var province: String ?= null,
    var phone: String ?= null,
    var latitude: Double ?= null,
    var longitude: Double ?= null,
    var price: String ?= null,
    var isChecked: Boolean = false
): Parcelable
