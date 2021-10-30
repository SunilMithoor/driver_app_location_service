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
import com.app.data.datasource.remote.RetrofitManager
import com.app.data.datasource.remote.SighInApi
import com.app.data.repository.SignInRepoImpl
import com.app.data.repository.UserDataRepoImpl
import com.app.domain.entity.wrapped.Event
import com.app.domain.manager.SignInUpdateManager
import com.app.domain.manager.UserPrefDataManager
import com.app.domain.repository.SignInRepo
import com.app.domain.repository.UserDataRepo
import com.app.domain.usecase.SignInUseCase
import com.app.extension.P
import com.app.vm.SharedVM
import com.app.vm.permission.PermissionVM
import com.app.vm.signin.SignInVM
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import timber.log.Timber

fun dependency() = listOf(vm, repository, manager, service, useCases, singleInstance)

val vm = module {
//    viewModel { DashboardVM(get()) }
    viewModel { SignInVM(get()) }
    viewModel { PermissionVM() }
    single { SharedVM() }
}
val useCases = module {
    factory { SignInUseCase(get()) }
}
val manager = module {
    single { UserPrefDataManager(get()) }
    single { SignInUpdateManager(get()) }
}
val repository = module {
    single { SignInRepoImpl(get()) as SignInRepo }
    single { UserDataRepoImpl(get(), get()) as UserDataRepo }
}
val service = module {
    single { SighInApi.create(get()) }
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
