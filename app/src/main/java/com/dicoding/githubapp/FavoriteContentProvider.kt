package com.dicoding.githubapp

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class FavoriteContentProvider : ContentProvider() {

    private val sUriMather = UriMatcher(UriMatcher.NO_MATCH)
    private lateinit var favoriteDao: FavoriteDao

    init {
        sUriMather.addURI(AUTHORITY, TABLE_NAME, FAVORITE)
    }

    override fun onCreate(): Boolean {
        val context = context ?: return false
        favoriteDao = AppDatabase.getInstance(context).favoriteDao()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        return runBlocking {
            withContext(Dispatchers.IO) {
                Log.d("FavoriteContentProvider", "runBlocking")
                getQueryFavorite()
            }
        }
    }

    private fun getQueryFavorite(): Cursor {
        val cursor = favoriteDao.contentProviders()
        Log.d("FavoriteContentProvider", "$cursor")

        cursor.moveToFirst()
        return cursor
    }


    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0


}
