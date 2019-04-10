package com.example.zhenghaozhang.findacat.petfinder

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoItem(
        @Json(name = "\$t") val t: String
): Parcelable