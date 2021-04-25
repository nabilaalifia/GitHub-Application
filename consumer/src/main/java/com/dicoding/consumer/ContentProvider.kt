package com.dicoding.consumer

import android.database.Cursor
import android.util.Log

object ContentProvider {

    fun mapCursorToFavorite(cursor: Cursor): GitHub? {

        var favorite: GitHub? = null
        try {
            favorite = GitHub(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(LOGIN)),
                cursor.getString(cursor.getColumnIndexOrThrow(NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(PUBLIC_REPOS)),
                cursor.getString(cursor.getColumnIndexOrThrow(COMPANY)),
                cursor.getString(cursor.getColumnIndexOrThrow(FOLLOWERS)),
                cursor.getString(cursor.getColumnIndexOrThrow(FOLLOWING)),
                cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_URL)),
                cursor.getString(cursor.getColumnIndexOrThrow(URL_FOLLOWERS)),
                cursor.getString(cursor.getColumnIndexOrThrow(URL_FOLLOWING)),

                )
        } catch (e: Exception) {
            Log.e("ContentProvider", "mapCursorToFavorite $e")
        }
        cursor.moveToNext()

        return favorite

    }
}