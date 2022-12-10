package com.dd.api.baseModel
import com.google.gson.annotations.SerializedName


data class CategoryResponseModel(
    @SerializedName("data")
    var `data`: List<CategoryResponseModelData>,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)

data class CategoryResponseModelData(
    @SerializedName("categoryDescription")
    var categoryDescription: String,
    @SerializedName("categoryName")
    var categoryName: String,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("_id")
    var id: String,
    @SerializedName("isActive")
    var isActive: Boolean,
    @SerializedName("updatedAt")
    var updatedAt: String,
    @SerializedName("__v")
    var v: Int
)