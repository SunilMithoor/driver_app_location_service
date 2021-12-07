package com.app.domain.usecase

import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId
import com.app.domain.entity.response.MQTTConnect
import com.app.domain.entity.response.MQTTData
import com.app.domain.manager.MQTTUpdateManager


class MQTTGenerateClientIdUseCase(
    private val mqttUpdateManager: MQTTUpdateManager
) {

    suspend operator fun invoke(): MQTTCallResponse<MQTTClientId>? =
        mqttUpdateManager.generateClientId()
}

class MQTTConnectedUseCase(
    private val mqttUpdateManager: MQTTUpdateManager
) {

    suspend operator fun invoke(): MQTTCallResponse<MQTTConnect>? =
        mqttUpdateManager.isConnected()
}

class MQTTConnectUseCase(
    private val mqttUpdateManager: MQTTUpdateManager
) {

    suspend operator fun invoke(username: String, password: String): MQTTCallResponse<MQTTData>? =
        mqttUpdateManager.connect(username, password)
}

class MQTTDisConnectUseCase(
    private val mqttUpdateManager: MQTTUpdateManager
) {

    suspend operator fun invoke() =
        mqttUpdateManager.disConnect()
}

class MQTTSubscribeUseCase(
    private val mqttUpdateManager: MQTTUpdateManager
) {

    suspend operator fun invoke() =
        mqttUpdateManager.subscribe()
}

class MQTTUnsubscribeUseCase(
    private val mqttUpdateManager: MQTTUpdateManager
) {

    suspend operator fun invoke() =
        mqttUpdateManager.unsubscribe()
}

class MQTTPublishUseCase(
    private val mqttUpdateManager: MQTTUpdateManager
) {

    suspend operator fun invoke(data: String) =
        mqttUpdateManager.publish(data)
}