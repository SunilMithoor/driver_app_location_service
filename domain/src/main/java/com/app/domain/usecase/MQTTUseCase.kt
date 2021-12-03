package com.app.domain.usecase

import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId
import com.app.domain.manager.MQTTUpdateManager


class MQTTGenerateClientIdUseCase(
    private val mqttUpdateManager: MQTTUpdateManager
) {

    suspend operator fun invoke(): MQTTCallResponse<MQTTClientId>? =
        mqttUpdateManager.generateClientId()
}

class MQTTConnectUseCase(
    private val mqttUpdateManager: MQTTUpdateManager
) {

    suspend operator fun invoke(): MQTTCallResponse<MQTTClientId>? =
        mqttUpdateManager.connect()
}

class MQTTDisConnectUseCase(
    private val mqttUpdateManager: MQTTUpdateManager
) {

    suspend operator fun invoke(): MQTTCallResponse<MQTTClientId>? =
        mqttUpdateManager.disConnect()
}

class MQTTSubscribeUseCase(
    private val mqttUpdateManager: MQTTUpdateManager
) {

    suspend operator fun invoke(): MQTTCallResponse<MQTTClientId>? =
        mqttUpdateManager.subscribe()
}

class MQTTUnsubscribeUseCase(
    private val mqttUpdateManager: MQTTUpdateManager
) {

    suspend operator fun invoke(): MQTTCallResponse<MQTTClientId>? =
        mqttUpdateManager.unsubscribe()
}

class MQTTPublishUseCase(
    private val mqttUpdateManager: MQTTUpdateManager
) {

    suspend operator fun invoke(): MQTTCallResponse<MQTTClientId>? =
        mqttUpdateManager.publish()
}