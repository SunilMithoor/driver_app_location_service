package com.app.data.datasource.remote.mqtt

import android.content.Context
import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.response.MQTTClientId
import com.app.domain.entity.response.MQTTConnect
import com.app.domain.entity.response.MQTTData
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import timber.log.Timber


class MQTTCall(context: Context) {

    val MQTT_SERVER_URI_KEY = "MQTT_SERVER_URI"
    val MQTT_CLIENT_ID_KEY = "MQTT_CLIENT_ID"
    val MQTT_USERNAME_KEY = "MQTT_USERNAME"
    val MQTT_PWD_KEY = "MQTT_PWD"

    //    val MQTT_SERVER_URI = "tcp://broker.hivemq.com:1883"
    val MQTT_SERVER_URI = "cd8bfcbefc0344e2ab3325a2ebd28194.s2.eu.hivemq.cloud:8884"

    //    val MQTT_SERVER_PORT = 8884
    val MQTT_CLIENT_ID = "clientId-br2xyGADsj"
    val MQTT_USERNAME = "sunilmg"
    val MQTT_PWD = "Sunil@135"
    val MQTT_TEST_TOPIC = "test_topic_2"
    val MQTT_TEST_MSG = "Hello!"
    val QOS = 2


    private var mqttClient: MqttAndroidClient? = null

    init {
        if (mqttClient == null) {
            val clientId = MqttClient.generateClientId()
//            mqttClient = MqttAndroidClient(context, MQTT_SERVER_URI, clientId)
            mqttClient = MqttAndroidClient(context, MQTT_SERVER_URI, MQTT_CLIENT_ID)
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

    fun isConnected(): MQTTCallResponse<MQTTConnect>? {
        return try {
            MQTTCallResponse.Success(MQTTConnect(mqttClient?.isConnected))
        } catch (exception: Exception) {
            MQTTCallResponse.Failure(exception)
        }
    }


    fun connects(
        username: String? = MQTT_USERNAME,
        password: String? = MQTT_PWD
    ) {
        try {
            mqttClient?.connect(getMqttConnectOptions(username, password),defaultCbConnect)

//            mqttClient?.setCallback(defaultCbClient)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun connect(
        username: String? = MQTT_USERNAME,
        password: String? = MQTT_PWD
    ): MQTTCallResponse<MQTTData>? {
        return try {
            val data =
                mqttClient?.connect(getMqttConnectOptions(username, password))
            MQTTCallResponse.Success(
                MQTTData(
                    data?.client?.clientId,
                    data?.client?.isConnected,
                    data?.client?.serverURI
                )
            )
        } catch (e: MqttException) {
            MQTTCallResponse.Failure(e)
        }
    }

    fun disConnect(): MQTTCallResponse<MQTTConnect>? {
        return try {
            mqttClient?.disconnect()
            MQTTCallResponse.Success(MQTTConnect(true))
        } catch (exception: Exception) {
            MQTTCallResponse.Failure(exception)
        }
    }

    fun subscribeTopic(): MQTTCallResponse<MQTTData>? {
        return try {
            val data =
                mqttClient?.subscribe(MQTT_TEST_TOPIC, QOS)
            MQTTCallResponse.Success(
                MQTTData(
                    data?.client?.clientId,
                    data?.client?.isConnected,
                    data?.client?.serverURI
                )
            )
        } catch (exception: Exception) {
            MQTTCallResponse.Failure(exception)
        }
    }

    fun unsubscribeTopic(): MQTTCallResponse<MQTTData>? {
        return try {
            val data =
                mqttClient?.unsubscribe(MQTT_TEST_TOPIC)
            MQTTCallResponse.Success(
                MQTTData(
                    data?.client?.clientId,
                    data?.client?.isConnected,
                    data?.client?.serverURI
                )
            )
        } catch (exception: Exception) {
            MQTTCallResponse.Failure(exception)
        }
    }

    fun publishMessage(data: String?): MQTTCallResponse<MQTTData>? {
        return try {
            val message = MqttMessage()
            message.payload = data?.toByteArray()
            val datas = mqttClient?.publish(MQTT_TEST_TOPIC, message)
            MQTTCallResponse.Success(
                MQTTData(
                    datas?.client?.clientId,
                    datas?.client?.isConnected,
                    datas?.client?.serverURI
                )
            )
        } catch (exception: Exception) {
            MQTTCallResponse.Failure(exception)
        }
    }

    private fun getMqttConnectOptions(username: String?, password: String?): MqttConnectOptions {
        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.userName = username
        mqttConnectOptions.password = password?.toCharArray()
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.isCleanSession = false
        return mqttConnectOptions
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