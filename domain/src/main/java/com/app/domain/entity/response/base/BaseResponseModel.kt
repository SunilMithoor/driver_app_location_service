package com.app.domain.entity.response.base

import kotlinx.serialization.Serializable

@Serializable
data class Failure(
    var message: String = "",
    var statusCode: Int = 0
)

open class Success {
    var message: String = ""
    var statusCode: Int = 0
}