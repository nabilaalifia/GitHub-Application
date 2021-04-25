package com.dicoding.githubapp

import android.database.Cursor
import androidx.room.*

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    fun getFavorites(): List<GitHub>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(favorite: GitHub)

    @Query("SELECT * FROM favorite WHERE id=:id ")
    fun getSingleFavorite(id: Int): GitHub?

    @Delete
    fun removeFavorite(favorite: GitHub)

    @Query("SELECT * FROM favorite")
    fun contentProviders(): Cursor
//

}