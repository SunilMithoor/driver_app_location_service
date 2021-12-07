package com.app.vm.mqtt

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId
import com.app.domain.entity.response.MQTTConnect
import com.app.domain.entity.response.MQTTData
import com.app.domain.usecase.*
import com.app.vm.BaseVM
import com.app.vm.UserEvent
import kotlinx.coroutines.launch


class MQTTVM(
    context: Context,
    private val mqttGenerateClientIdUseCase: MQTTGenerateClientIdUseCase,
    private val mqttConnectUseCase: MQTTConnectUseCase,
    private val mqttConnectedUseCase: MQTTConnectedUseCase,
    private val mqttDisConnectUseCase: MQTTDisConnectUseCase,
    private val mqttSubscribeUseCase: MQTTSubscribeUseCase,
    private val mqttUnsubscribeUseCase: MQTTUnsubscribeUseCase,
    private val mqttPublishUseCase: MQTTPublishUseCase,

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

    private val _mqttConnectedResponse = MutableLiveData<MQTTCallResponse<MQTTConnect>?>()
    val mqttConnectedResponse get() = _mqttConnectedResponse


    fun isConnected() =
        viewModelScope.launch {
            _mqttConnectedResponse.postValue(
                mqttConnectedUseCase.invoke()
            )
        }


    private val _mqttConnectResponse = MutableLiveData<MQTTCallResponse<MQTTData>?>()
    val mqttConnectResponse get() = _mqttConnectResponse


    fun connectMQTT(username: String, password: String) =
        viewModelScope.launch {
            _mqttConnectResponse.postValue(
                mqttConnectUseCase.invoke(username, password)
            )
        }

    private val _mqttDisConnectResponse = MutableLiveData<MQTTCallResponse<MQTTConnect>?>()
    val mqttDisConnectResponse get() = _mqttDisConnectResponse


    fun disConnectMQTT() =
        viewModelScope.launch {
            _mqttDisConnectResponse.postValue(
                mqttDisConnectUseCase.invoke()
            )
        }

    private val _mqttSubscribeResponse = MutableLiveData<MQTTCallResponse<MQTTData>?>()
    val mqttSubscribeResponse get() = _mqttSubscribeResponse


    fun subscribeMQTT() =
        viewModelScope.launch {
            _mqttSubscribeResponse.postValue(
                mqttSubscribeUseCase.invoke()
            )
        }

    private val _mqttUnSubscribeResponse = MutableLiveData<MQTTCallResponse<MQTTData>?>()
    val mqttUnSubscribeResponse get() = _mqttUnSubscribeResponse


    fun unsubscribeMQTT() =
        viewModelScope.launch {
            _mqttUnSubscribeResponse.postValue(
                mqttUnsubscribeUseCase.invoke()
            )
        }

    private val _mqttPublishResponse = MutableLiveData<MQTTCallResponse<MQTTData>?>()
    val mqttPublishResponse get() = _mqttPublishResponse


    fun publishMQTT(data: String) =
        viewModelScope.launch {
            _mqttPublishResponse.postValue(
                mqttPublishUseCase.invoke(data)
            )
        }

}