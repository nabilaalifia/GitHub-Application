package com.dicoding.consumer

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorite")
data class GitHub(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = LOGIN) var username: String,
    @ColumnInfo(name = NAME) var name: String,
    @ColumnInfo(name = LOCATION) var location: String,
    @ColumnInfo(name = PUBLIC_REPOS) var repository: String,
    @ColumnInfo(name = COMPANY) var company: String,
    @ColumnInfo(name = FOLLOWERS) var followers: String,
    @ColumnInfo(name = FOLLOWING) var following: String,
    @ColumnInfo(name = AVATAR_URL) var photo: String,
    @ColumnInfo(name = URL_FOLLOWERS) var url_followers: String,
    @ColumnInfo(name = URL_FOLLOWING) var url_following: String,
    @ColumnInfo(name = IS_FAVORITE) var isFavorite: Boolean = false
) : Parcelable

const val AUTHORITY = "com.dicoding.githubapp"
const val SCHEME = "content"
const val FAVORITE = 1

const val ID = "id"
const val TABLE_NAME = "db_favorite"
const val IS_FAVORITE = "is_favorite"
const val LOGIN = "login"
const val COMPANY = "company"
const val PUBLIC_REPOS = "publicRepos"
const val FOLLOWERS = "followers"
const val AVATAR_URL = "avatarUrl"
const val FOLLOWING = "following"
const val NAME = "name"
const val LOCATION = "location"
const val URL_FOLLOWERS = "url follower"
const val URL_FOLLOWING = "url following"