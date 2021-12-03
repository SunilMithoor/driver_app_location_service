package com.app.data.datasource.remote.mqtt

import android.content.Context
import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import timber.log.Timber


class MQTTCall(context: Context) {

    val MQTT_SERVER_URI_KEY = "MQTT_SERVER_URI"
    val MQTT_CLIENT_ID_KEY = "MQTT_CLIENT_ID"
    val MQTT_USERNAME_KEY = "MQTT_USERNAME"
    val MQTT_PWD_KEY = "MQTT_PWD"
    val MQTT_SERVER_URI = "tcp://broker.hivemq.com:1883"
    val MQTT_CLIENT_ID = ""
    val MQTT_USERNAME = ""
    val MQTT_PWD = ""
    val MQTT_TEST_TOPIC = "test/topic"
    val MQTT_TEST_MSG = "Hello!"


    private var mqttClient: MqttAndroidClient? = null

    init {
        if (mqttClient == null) {
            val clientId = MqttClient.generateClientId()
            mqttClient = MqttAndroidClient(context, MQTT_SERVER_URI, clientId)
        }
    }

    fun generateClientId(): MQTTCallResponse<MQTTClientId>? {
        return try {
            val clientId = MqttClient.generateClientId()
            MQTTCallResponse.Success(MQTTClientId(clientId))
        } catch (exception: Exception) {
            MQTTCallResponse.Failure(exception)
        }
    }

    fun isConnected(): Boolean {
        return mqttClient?.isConnected == true
    }


//    fun connect(
//        username: String = "",
//        password: String = "",
//        cbConnect: IMqttActionListener = defaultCbConnect,
//        cbClient: MqttCallback = defaultCbClient
//    ): MQTTCallResponse<MQTTClientId>? {
//        return try {
////            mqttClient?.setCallback(cbClient)
//            val options = MqttConnectOptions()
//            options.userName = username
//            options.password = password.toCharArray()
//            val connected = mqttClient?.connect(options, null, cbConnect)
//            MQTTCallResponse.Success(MQTTClientId(connected.toString()))
//        } catch (exception: Exception) {
//            MQTTCallResponse.Failure(exception)
//        }
//    }

    fun connect(): MQTTCallResponse<MQTTClientId>? {
        return try {
            var tokens: IMqttToken? = null
            val token = mqttClient?.connect()
            token?.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // We are connected
                    Timber.d("onSuccess")
                    val options = MqttConnectOptions()
                    options.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1
                    tokens = mqttClient?.connect(options)
                    Timber.d("Token-->${tokens?.client}")
                    Timber.d("Token-->${tokens?.messageId}")
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Timber.d("onFailure")
                }
            }
            return MQTTCallResponse.Success(MQTTClientId(tokens.toString()))
        } catch (e: MqttException) {
            e.printStackTrace()
            MQTTCallResponse.Failure(e)
        }
    }


    fun disConnect(): MQTTCallResponse<MQTTClientId>? {
        return try {
            val clientId = MqttClient.generateClientId()
            MQTTCallResponse.Success(MQTTClientId(clientId))
        } catch (exception: Exception) {
            MQTTCallResponse.Failure(exception)
        }
    }


    fun subscribe(): MQTTCallResponse<MQTTClientId>? {
        return try {
            val clientId = MqttClient.generateClientId()
            MQTTCallResponse.Success(MQTTClientId(clientId))
        } catch (exception: Exception) {
            MQTTCallResponse.Failure(exception)
        }
    }

    fun unsubscribe(): MQTTCallResponse<MQTTClientId>? {
        return try {
            val clientId = MqttClient.generateClientId()
            MQTTCallResponse.Success(MQTTClientId(clientId))
        } catch (exception: Exception) {
            MQTTCallResponse.Failure(exception)
        }
    }

    fun publish(): MQTTCallResponse<MQTTClientId>? {
        return try {
            val clientId = MqttClient.generateClientId()
            MQTTCallResponse.Success(MQTTClientId(clientId))
        } catch (exception: Exception) {
            MQTTCallResponse.Failure(exception)
        }
    }


    private val defaultCbConnect = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Timber.d("(Default) Connection success")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Timber.d("Connection failure: ${exception.toString()}")
        }
    }

    private val defaultCbClient = object : MqttCallback {
        override fun messageArrived(topic: String?, message: MqttMessage?) {
            Timber.d("Receive message: ${message.toString()} from topic: $topic")
        }

        override fun connectionLost(cause: Throwable?) {
            Timber.d("Connection lost ${cause.toString()}")
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            Timber.d("Delivery completed")
        }
    }

}