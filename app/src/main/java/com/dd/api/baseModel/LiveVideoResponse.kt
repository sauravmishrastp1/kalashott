package com.dd.api.baseModel
import com.google.gson.annotations.SerializedName


data class LiveVideoResponse(
    @SerializedName("data")
    var `data`: List<LiveVideoResponseData>,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)

data class LiveVideoResponseData(
    @SerializedName("categories")
    var categories: List<Any>,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("_id")
    var id: String,
    @SerializedName("isLive")
    var isLive: Boolean,
    @SerializedName("thumbnail")
    var thumbnail: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("updatedAt")
    var updatedAt: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("__v")
    var v: Int
)