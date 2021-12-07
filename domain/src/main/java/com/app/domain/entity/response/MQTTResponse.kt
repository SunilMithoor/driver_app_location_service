package com.app.domain.entity.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage

@Parcelize
data class MQTTClientId(
    val data: String?,
) : Parcelable

@Parcelize
data class MQTTConnect(
    val data: Boolean?,
) : Parcelable


data class MQTTData(
    val clientId: String?,
    val isConnected: Boolean?,
    val serverURI: String?
)
