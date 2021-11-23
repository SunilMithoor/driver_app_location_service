package com.app.domain.interactor


import com.app.domain.entity.request.FirebaseDatabaseRequest
import com.app.domain.usecase.DatabaseFirebaseUseCase

class FirebaseDatabaseInteractor(
    private val databaseFirebaseUseCase: DatabaseFirebaseUseCase
) {


    suspend fun setDatabaseData(firebaseDatabaseRequest: FirebaseDatabaseRequest) {
        databaseFirebaseUseCase.invoke(firebaseDatabaseRequest)
    }


}
