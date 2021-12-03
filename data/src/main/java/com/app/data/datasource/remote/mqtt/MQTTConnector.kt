package com.app.data.datasource.remote.mqtt

import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId
import com.app.domain.repository.MQTTDataRepo


class MQTTConnector(
    private val mqttCall: MQTTCall
) : MQTTDataRepo {

    override suspend fun generateClientId(): MQTTCallResponse<MQTTClientId>? =
        mqttCall.generateClientId()

    override suspend fun connect(): MQTTCallResponse<MQTTClientId>? =
        mqttCall.connect()

    override suspend fun disConnect(): MQTTCallResponse<MQTTClientId>? =
        mqttCall.disConnect()

    override suspend fun subscribe(): MQTTCallResponse<MQTTClientId>? =
        mqttCall.subscribe()

    override suspend fun unsubscribe(): MQTTCallResponse<MQTTClientId>? =
        mqttCall.unsubscribe()

    override suspend fun publish(): MQTTCallResponse<MQTTClientId>? =
        mqttCall.publish()


}