package com.app.vm.mqtt

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId
import com.app.domain.usecase.MQTTConnectUseCase
import com.app.domain.usecase.MQTTGenerateClientIdUseCase
import com.app.vm.BaseVM
import com.app.vm.UserEvent
import kotlinx.coroutines.launch


class MQTTVM(
    context: Context,
    private val mqttGenerateClientIdUseCase: MQTTGenerateClientIdUseCase,
    private val mqttConnectUseCase: MQTTConnectUseCase
) : BaseVM() {


    override fun onAction(event: UserEvent) {
        when (event) {


        }
    }

    private val _mqttGenerateClientIdResponse =
        MutableLiveData<MQTTCallResponse<MQTTClientId>?>()
    val mqttGenerateClientIdResponse get() = _mqttGenerateClientIdResponse


    fun getMQTTClientId() =
        viewModelScope.launch {
            _mqttGenerateClientIdResponse.postValue(
                mqttGenerateClientIdUseCase.invoke()
            )
        }

    private val _mqttConnectResponse =
        MutableLiveData<MQTTCallResponse<MQTTClientId>?>()
    val mqttConnectResponse get() = _mqttConnectResponse


    fun connectMQTT(username:String,password:String) =
        viewModelScope.launch {
            _mqttConnectResponse.postValue(
                mqttConnectUseCase.invoke()
            )
        }


}