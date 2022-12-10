package com.dd.api.baseModel
import com.google.gson.annotations.SerializedName


data class SearchResponse(
    @SerializedName("data")
    var `data`: List<SearchResponseData>,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)

data class SearchResponseData(
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
    var tags: List<SearchResponseDataTag>,
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

data class SearchResponseDataTag(
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("_id")
    var id: String,
    @SerializedName("isActive")
    var isActive: Boolean,
    @SerializedName("tagName")
    var tagName: String,
    @SerializedName("updatedAt")
    var updatedAt: String,
    @SerializedName("__v")
    var v: Int
)