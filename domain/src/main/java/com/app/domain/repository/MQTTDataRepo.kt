package com.app.domain.repository

import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId
import com.app.domain.entity.response.MQTTConnect
import com.app.domain.entity.response.MQTTData


interface MQTTDataRepo {

    suspend fun generateClientId(): MQTTCallResponse<MQTTClientId>?

    suspend fun isConnected(): MQTTCallResponse<MQTTConnect>?

    suspend fun connect(username: String?, password: String?): MQTTCallResponse<MQTTData>?

    suspend fun disConnect(): MQTTCallResponse<MQTTConnect>?

    suspend fun subscribe(): MQTTCallResponse<MQTTData>?

    suspend fun unsubscribe(): MQTTCallResponse<MQTTData>?

    suspend fun publish(data:String?): MQTTCallResponse<MQTTData>?

}
