package com.dicoding.submission.githubfavorituser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.submission.githubfavorituser.local.db.FavoritDao
import com.dicoding.submission.githubfavorituser.local.db.FavoritDatabase
import com.dicoding.submission.githubfavorituser.local.model.UserSearch

class FavoritViewModel(application: Application): AndroidViewModel(application) {
    private  var favoritDao: FavoritDao?
    private var favoritDb: FavoritDatabase?

    init {
        favoritDb = FavoritDatabase.getDatabase(application)
        favoritDao = favoritDb?.favoritDao()
    }

    fun getFavorite(): LiveData<List<UserSearch>>? {
        return favoritDao?.getFavoriteUser()
    }
}