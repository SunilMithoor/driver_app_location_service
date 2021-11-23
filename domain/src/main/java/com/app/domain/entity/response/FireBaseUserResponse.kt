package com.app.domain.entity.response

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FireBaseAuthUser(
    val uid: String?,
    val providerId: String?,
    val displayName: String?,
    val email: String?,
    val photoUrl: Uri?,
    val phoneNumber: String?,
    val isEmailVerified: Boolean?,
) : Parcelable

@Parcelize
data class FireBaseMessageToken(
    val token: String?,
) : Parcelable

@Parcelize
data class FireBaseDeviceId(
    val deviceId: String?,
) : Parcelable


@Parcelize
data class FireBaseMessage(
    val signOut: Boolean?,
) : Parcelable


@Parcelize
data class FireBaseDatabase(
    val data: String?,
) : Parcelable
