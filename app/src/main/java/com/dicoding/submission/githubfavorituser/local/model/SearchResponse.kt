package com.dicoding.submission.githubfavorituser.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponse(

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("items")
    val usersSearch: List<UserSearch>

) : Parcelable

@Parcelize
@Entity(tableName = "favorite_users")
data class UserSearch(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @PrimaryKey
    @field:SerializedName("id")
    val id: Int

    ) : Parcelable