package com.app.di


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import coil.ImageLoaderBuilder
import coil.util.Logger
import com.app.BuildConfig
import com.app.data.datasource.db.AppDatabase
import com.app.data.datasource.remote.firebase.auth.FirebaseAuthCall
import com.app.data.datasource.remote.firebase.auth.FirebaseAuthenticator
import com.app.data.datasource.remote.firebase.database.FireBaseDatabaseCall
import com.app.data.datasource.remote.firebase.deviceid.FirebaseDeviceIdCall
import com.app.data.datasource.remote.firebase.message.FirebaseMessageCall
import com.app.data.datasource.remote.mqtt.MQTTCall
import com.app.data.datasource.remote.mqtt.MQTTConnector
import com.app.data.datasource.remote.retrofit.OnBoardingApi
import com.app.data.datasource.remote.retrofit.RetrofitManager
import com.app.data.repository.*
import com.app.domain.entity.wrapped.Event
import com.app.domain.interactor.FirebaseDatabaseInteractor
import com.app.domain.interactor.LocationServiceInteractor
import com.app.domain.manager.FirebaseUpdateManager
import com.app.domain.manager.MQTTUpdateManager
import com.app.domain.manager.OnBoardingUpdateManager
import com.app.domain.manager.UserPrefDataManager
import com.app.domain.repository.*
import com.app.domain.usecase.*
import com.app.extension.P
import com.app.vm.SharedVM
import com.app.vm.location.LocationVM
import com.app.vm.mqtt.MQTTVM
import com.app.vm.onboarding.OnBoardingVM
import com.app.vm.permission.PermissionVM
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import timber.log.Timber

fun dependency() = listOf(
    vm, repository, manager, service, useCases, dataBase,
    serviceInteractor, fireBase, mqtt, singleInstance
)

val vm = module {
    viewModel { OnBoardingVM(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { PermissionVM() }
    viewModel { LocationVM(get(), get(), get(), get(), get()) }
    viewModel { MQTTVM(get(), get(),get()) }
    single { SharedVM() }
}
val useCases = module {
    factory { SignInUseCase(get()) }
    factory { SignInFirebaseUseCase(get()) }
    factory { SignUpFirebaseUseCase(get()) }
    factory { GetUserFirebaseUseCase(get()) }
    factory { SignOutFirebaseUseCase(get()) }
    factory { ResetPasswordFirebaseUseCase(get()) }
    factory { MessageTokenFirebaseUseCase(get()) }
    factory { DeviceIdFirebaseUseCase(get()) }
    factory { InsertLocationUseCase(get()) }
    factory { GetAllLocationUseCase(get()) }
    factory { GetLocationByCountUseCase(get()) }
    factory { DeleteAllLocationUseCase(get()) }
    factory { DeleteLocationByCountUseCase(get()) }
    factory { DatabaseFirebaseUseCase(get()) }
    factory { MQTTGenerateClientIdUseCase(get()) }
    factory { MQTTConnectUseCase(get()) }
    factory { MQTTDisConnectUseCase(get()) }
    factory { MQTTSubscribeUseCase(get()) }
    factory { MQTTUnsubscribeUseCase(get()) }
    factory { MQTTPublishUseCase(get()) }
}
val manager = module {
    single { UserPrefDataManager(get()) }
    single { OnBoardingUpdateManager(get(), get()) }
    single { FirebaseUpdateManager(get(), get()) }
    single { MQTTUpdateManager(get(), get()) }
}
val repository = module {
    single { OnBoardingRepoImpl(get()) as OnBoardingRepo }
    single { UserDataRepoImpl(get(), get()) as UserDataRepo }
    single { LocationRepoImpl(get()) as LocationDataRepo }
    single { FirebaseRepoImpl(get()) as FirebaseDataRepo }
    single { MQTTRepoImpl(get()) as MQTTDataRepo }
}
val service = module {
    single { OnBoardingApi.create(get()) }
}
val dataBase = module {
    single { AppDatabase.getInstance(get()) }
    single { get<AppDatabase>().appDao() }
}
val serviceInteractor = module {
    single { LocationServiceInteractor(get(), get(), get(), get(), get()) }
    single { FirebaseDatabaseInteractor(get()) }
}

val fireBase = module {
    single { FirebaseAuthCall() }
    single { FirebaseMessageCall() }
    single { FirebaseDeviceIdCall() }
    single { FireBaseDatabaseCall() }
    single { FirebaseAuthenticator(get(), get(), get(), get()) }
}

val mqtt = module {
    single { MQTTCall(get()) }
    single { MQTTConnector(get()) }
}

val singleInstance = module {
    single {
        ImageLoaderBuilder(get()).apply {
            availableMemoryPercentage(0.25)
            crossfade(true)
            logger(object : Logger {
                override var level: Int
                    get() = 1
                    set(value) {
                    }

                override fun log(
                    tag: String,
                    priority: Int,
                    message: String?,
                    throwable: Throwable?
                ) {
                    Timber.d(message)
                }

            })
        }.build()
    }
    single {
        val manager = RetrofitManager(get())
        manager.retrofit(BuildConfig.BASE_URL)
        manager
    }
    single { get<RetrofitManager>().retrofit(BuildConfig.BASE_URL) }
    single { P.customPrefs(get(), BuildConfig.APPLICATION_ID) }
    single {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
    }
    single { AppUpdateManagerFactory.create(get()) }
    single { MutableLiveData<Event<Bundle>>() }
    single {
        val notificationManager: NotificationManager =
            get<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel_App"
            val descriptionText = "Channel_App_Desc"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Channel_App", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system

            notificationManager.createNotificationChannel(channel)
        }
        notificationManager
    }
}
