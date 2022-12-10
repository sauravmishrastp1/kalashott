package com.dd.samplevideoplayer.ui.home.model
import com.google.gson.annotations.SerializedName


data class GetLiveBanner(
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)

data class Data(
    @SerializedName("_id")
    var id: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("url")
    var url: String
)