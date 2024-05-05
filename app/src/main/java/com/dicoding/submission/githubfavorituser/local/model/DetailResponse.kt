package com.dicoding.submission.githubfavorituser.local.model

import com.google.gson.annotations.SerializedName

data class DetailResponse(

    @field:SerializedName("bio")
    val bio: Any? = null,

    @field:SerializedName("login")
    val login: String? = null,

    @field:SerializedName("followers")
    val followers: Int? = null,

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @field:SerializedName("html_url")
    val htmlUrl: String? = null,

    @field:SerializedName("following")
    val following: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,
)