package com.dd.samplevideoplayer.ui.about.model
import com.google.gson.annotations.SerializedName


data class About(
    @SerializedName("description")
    var description: String,
    @SerializedName("persons")
    var persons: List<Person>,
    @SerializedName("title")
    var title: String
)

data class Person(
    @SerializedName("img")
    var img: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("title")
    var title: String
)