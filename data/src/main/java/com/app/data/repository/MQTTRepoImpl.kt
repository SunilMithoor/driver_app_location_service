package com.app.data.repository

import com.app.data.datasource.remote.mqtt.MQTTConnector
import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId
import com.app.domain.repository.MQTTDataRepo


class MQTTRepoImpl(
    private val mqttConnector: MQTTConnector
) : MQTTDataRepo {

    override suspend fun generateClientId(): MQTTCallResponse<MQTTClientId>? =
        mqttConnector.generateClientId()

    override suspend fun connect(): MQTTCallResponse<MQTTClientId>? =
        mqttConnector.connect()


    override suspend fun disConnect(): MQTTCallResponse<MQTTClientId>? =
        mqttConnector.disConnect()


    override suspend fun subscribe(): MQTTCallResponse<MQTTClientId>? =
        mqttConnector.subscribe()


    override suspend fun unsubscribe(): MQTTCallResponse<MQTTClientId>? =
        mqttConnector.unsubscribe()


    override suspend fun publish(): MQTTCallResponse<MQTTClientId>? =
        mqttConnector.publish()


}
