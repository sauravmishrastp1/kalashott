package com.dd.api.baseModel

import com.google.gson.annotations.SerializedName


data class MarkupeTextRespponse(
    @SerializedName("data")
    var data: List<MarkupeTextRespponseData>,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)

data class MarkupeTextRespponseData(
    @SerializedName("activeUntil")
    var activeUntil: String,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("_id")
    var id: String,
    @SerializedName("isVisible")
    var isVisible: Boolean,
    @SerializedName("text")
    var text: String,
    @SerializedName("updatedAt")
    var updatedAt: String,
    @SerializedName("__v")
    var v: Int
)