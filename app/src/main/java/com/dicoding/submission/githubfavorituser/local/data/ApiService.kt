package com.dicoding.submission.githubfavorituser.local.data

import com.dicoding.submission.githubfavorituser.BuildConfig
import com.dicoding.submission.githubfavorituser.local.model.DetailResponse
import com.dicoding.submission.githubfavorituser.local.model.SearchResponse
import com.dicoding.submission.githubfavorituser.local.model.UserSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun searchUser(
        @Query("q") login: String
    ): Call<SearchResponse>

    @GET("users/{login}")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getUserDetail(
        @Path("login") login: String
    ): Call<DetailResponse>

    @GET("users/{login}/followers")
    @Headers("Authorization: token  ${BuildConfig.GITHUB_TOKEN}")
    fun getUserFollowers(
        @Path("login") login: String
    ): Call<List<UserSearch>>

    @GET("users/{login}/following")
    @Headers("Authorization: token  ${BuildConfig.GITHUB_TOKEN}")
    fun getUserFollowings(
        @Path("login") login: String
    ): Call<List<UserSearch>>
}