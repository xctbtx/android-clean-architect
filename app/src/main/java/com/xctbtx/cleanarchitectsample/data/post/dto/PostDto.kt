package com.xctbtx.cleanarchitectsample.data.post.dto

import com.google.gson.annotations.SerializedName

data class PostDto(
    val id: String,
    val title: String,
    val content: String,
    @SerializedName("image_url")
    val image_url: String? = null
)