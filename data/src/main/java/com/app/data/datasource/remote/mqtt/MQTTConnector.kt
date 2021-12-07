package com.app.data.datasource.remote.mqtt

import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId
import com.app.domain.entity.response.MQTTConnect
import com.app.domain.entity.response.MQTTData
import com.app.domain.repository.MQTTDataRepo


class MQTTConnector(
    private val mqttCall: MQTTCall
) : MQTTDataRepo {

    override suspend fun generateClientId(): MQTTCallResponse<MQTTClientId>? =
        mqttCall.generateClientId()

    override suspend fun isConnected(): MQTTCallResponse<MQTTConnect>? =
        mqttCall.isConnected()

    override suspend fun connect(
        username: String?,
        password: String?
    ): MQTTCallResponse<MQTTData>? =
        mqttCall.connect(username, password)


    override suspend fun disConnect(): MQTTCallResponse<MQTTConnect>? =
        mqttCall.disConnect()


    override suspend fun subscribe(): MQTTCallResponse<MQTTData>? =
        mqttCall.subscribeTopic()


    override suspend fun unsubscribe(): MQTTCallResponse<MQTTData>? =
        mqttCall.unsubscribeTopic()


    override suspend fun publish(data: String?): MQTTCallResponse<MQTTData>? =
        mqttCall.publishMessage(data)


}