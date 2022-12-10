package com.dd.api.baseModel
import com.google.gson.annotations.SerializedName


data class CatWiseVideoResponse(
    @SerializedName("data")
    var `data`: List<CatWiseVideoResponseData>,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)

data class CatWiseVideoResponseData(
    @SerializedName("categories")
    var categories: String,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("duration")
    var duration: String,
    @SerializedName("_id")
    var id: String,
    @SerializedName("isVisible")
    var isVisible: Boolean,
    @SerializedName("showOnCategoryScreen")
    var showOnCategoryScreen: Boolean,
    @SerializedName("showOnHomeScreen")
    var showOnHomeScreen: Boolean,
    @SerializedName("tags")
    var tags: List<String>,
    @SerializedName("thumbnail")
    var thumbnail: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("updatedAt")
    var updatedAt: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("__v")
    var v: Int,
    @SerializedName("views")
    var views:Int
)