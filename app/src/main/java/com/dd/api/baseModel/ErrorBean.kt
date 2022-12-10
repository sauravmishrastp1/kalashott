package com.bizbrolly.wayja.api.baseModel

import com.google.gson.annotations.SerializedName

data class ErrorBean(
    @SerializedName("Message")
    val errorMessage: String,
    @SerializedName("Details")
    val result: Any,
    @SerializedName("HTTPStatusCode")
    val statusCode: Int

)
