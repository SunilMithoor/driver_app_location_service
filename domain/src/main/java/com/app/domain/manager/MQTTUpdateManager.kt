package com.app.domain.manager

import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId
import com.app.domain.repository.MQTTDataRepo
import com.app.domain.repository.UserDataRepo


class MQTTUpdateManager(
    private val mqttDataRepo: MQTTDataRepo,
    private val userDataRepo: UserDataRepo
) : MQTTDataRepo {

    override suspend fun generateClientId(): MQTTCallResponse<MQTTClientId>? =
        mqttDataRepo.generateClientId()

    override suspend fun connect(): MQTTCallResponse<MQTTClientId>? =
        mqttDataRepo.connect()

    override suspend fun disConnect(): MQTTCallResponse<MQTTClientId>? =
        mqttDataRepo.disConnect()

    override suspend fun subscribe(): MQTTCallResponse<MQTTClientId>? =
        mqttDataRepo.subscribe()

    override suspend fun unsubscribe(): MQTTCallResponse<MQTTClientId>? =
        mqttDataRepo.unsubscribe()

    override suspend fun publish(): MQTTCallResponse<MQTTClientId>? =
        mqttDataRepo.publish()


}
