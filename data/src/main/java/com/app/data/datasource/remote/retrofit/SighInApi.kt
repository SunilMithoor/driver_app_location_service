package com.app.data.datasource.remote.retrofit

import com.app.domain.entity.response.SignInUpdate
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface SighInApi {

    @POST("/api/driver/login")
    suspend fun signInUpdate(
        @Body map: Map<String, String>
    ): Response<SignInUpdate>

    companion object {
        fun create(retrofit: Retrofit): SighInApi = retrofit.create(SighInApi::class.java)
    }
}