package com.dicoding.submission.githubfavorituser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.submission.githubfavorituser.local.data.ApiConfig
import com.dicoding.submission.githubfavorituser.local.db.FavoritDao
import com.dicoding.submission.githubfavorituser.local.db.FavoritDatabase
import com.dicoding.submission.githubfavorituser.local.model.DetailResponse
import com.dicoding.submission.githubfavorituser.local.model.UserSearch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _listDetail = MutableLiveData<DetailResponse>()
    val listDetail: LiveData<DetailResponse> = _listDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var favoritDao: FavoritDao?
    private var favoritDb: FavoritDatabase?

    init {
        favoritDb = FavoritDatabase.getDatabase(application)
        favoritDao = favoritDb?.favoritDao()
    }

    companion object {
        private const val TAG = "UserDetailModel"
    }

    internal fun getGithubUser(login: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(login)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listDetail.value = response.body()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun addFavorite(login: String, avatarUrl: String, htmlUrl: String, id: Int,){
        CoroutineScope(Dispatchers.IO).launch {
            var user = UserSearch(
                login,
                avatarUrl,
                htmlUrl,
                id

            )
            favoritDao?.addToFavorite(user)
        }
    }

    fun checkUser(id: Int) = favoritDao?.checkUser(id)

    fun removeFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            favoritDao?.removeFavorite(id)
        }
    }
}