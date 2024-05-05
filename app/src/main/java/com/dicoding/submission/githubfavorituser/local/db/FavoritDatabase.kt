package com.dicoding.submission.githubfavorituser.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.submission.githubfavorituser.local.model.UserSearch

@Database(
    entities = [UserSearch::class],
    version = 1,
    exportSchema = false
)
abstract class FavoritDatabase : RoomDatabase() {
    abstract fun favoritDao(): FavoritDao

    companion object {
        var INSTANCE: FavoritDatabase? = null

        fun getDatabase(context: Context): FavoritDatabase? {
            if (INSTANCE == null){
                synchronized(FavoritDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, FavoritDatabase::class.java, "favoritUserGithub.db").build()
                }
            }
            return INSTANCE
        }
    }
}