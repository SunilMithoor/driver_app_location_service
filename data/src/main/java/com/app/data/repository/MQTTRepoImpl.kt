package com.app.data.repository

import com.app.data.datasource.remote.mqtt.MQTTConnector
import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId
import com.app.domain.entity.response.MQTTConnect
import com.app.domain.entity.response.MQTTData
import com.app.domain.repository.MQTTDataRepo


class MQTTRepoImpl(
    private val mqttConnector: MQTTConnector
) : MQTTDataRepo {

    override suspend fun generateClientId(): MQTTCallResponse<MQTTClientId>? =
        mqttConnector.generateClientId()

    override suspend fun isConnected(): MQTTCallResponse<MQTTConnect>? =
        mqttConnector.isConnected()

    override suspend fun connect(
        username: String?,
        password: String?
    ): MQTTCallResponse<MQTTData>? = mqttConnector.connect(username, password)


    override suspend fun disConnect(): MQTTCallResponse<MQTTConnect>? =
        mqttConnector.disConnect()


    override suspend fun subscribe(): MQTTCallResponse<MQTTData>? =
        mqttConnector.subscribe()


    override suspend fun unsubscribe(): MQTTCallResponse<MQTTData>? =
        mqttConnector.unsubscribe()


    override suspend fun publish(data: String?): MQTTCallResponse<MQTTData>? =
        mqttConnector.publish(data)

}
