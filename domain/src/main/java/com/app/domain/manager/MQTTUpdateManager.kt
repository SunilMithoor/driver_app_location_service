package com.app.domain.manager

import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId
import com.app.domain.entity.response.MQTTConnect
import com.app.domain.entity.response.MQTTData
import com.app.domain.repository.MQTTDataRepo
import com.app.domain.repository.UserDataRepo


class MQTTUpdateManager(
    private val mqttDataRepo: MQTTDataRepo,
    private val userDataRepo: UserDataRepo
) : MQTTDataRepo {

    override suspend fun generateClientId(): MQTTCallResponse<MQTTClientId>? =
        mqttDataRepo.generateClientId()

    override suspend fun isConnected(): MQTTCallResponse<MQTTConnect>? =
        mqttDataRepo.isConnected()

    override suspend fun connect(
        username: String?,
        password: String?
    ): MQTTCallResponse<MQTTData>? =
        mqttDataRepo.connect(username, password)


    override suspend fun disConnect(): MQTTCallResponse<MQTTConnect>? =
        mqttDataRepo.disConnect()

    override suspend fun subscribe(): MQTTCallResponse<MQTTData>? =
        mqttDataRepo.subscribe()

    override suspend fun unsubscribe(): MQTTCallResponse<MQTTData>? =
        mqttDataRepo.unsubscribe()

    override suspend fun publish(data: String?) =
        mqttDataRepo.publish(data)


}
