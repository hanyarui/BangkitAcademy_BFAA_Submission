package com.dicoding.submission.githubfavorituser.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.submission.githubfavorituser.local.model.UserSearch

@Dao
interface FavoritDao {
    @Insert
    suspend fun addToFavorite(favoriteUser: UserSearch)

    @Query("SELECT * FROM favorite_users")
    fun getFavoriteUser(): LiveData<List<UserSearch>>

    @Query("SELECT count(*) FROM favorite_users WHERE favorite_users.id = :id")
    fun checkUser(id: Int): Int

    @Query("DELETE FROM favorite_users WHERE favorite_users.id = :id")
    suspend fun removeFavorite(id: Int): Int

}