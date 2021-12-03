package com.app.domain.repository

import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId


interface MQTTDataRepo {

    suspend fun generateClientId(): MQTTCallResponse<MQTTClientId>?

    suspend fun connect(): MQTTCallResponse<MQTTClientId>?

    suspend fun disConnect(): MQTTCallResponse<MQTTClientId>?

    suspend fun subscribe(): MQTTCallResponse<MQTTClientId>?

    suspend fun unsubscribe(): MQTTCallResponse<MQTTClientId>?

    suspend fun publish(): MQTTCallResponse<MQTTClientId>?

}
