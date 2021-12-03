package com.app.domain.entity.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MQTTClientId(
    val data: String?,
) : Parcelable
