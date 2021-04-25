package com.dicoding.consumer

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.consumer.ContentProvider.mapCursorToFavorite


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionbar = supportActionBar
        actionbar?.title = "Favorite Consumer"


        listGithub()
        getList()

        LoaderManager.getInstance(this).initLoader(FAVORITE, null, mLoaderCallbacks)

    }

    private lateinit var adapter: CardViewGithubAdapter


    private fun getList() {
//        CoroutineScope(GlobalScope.coroutineContext).launch {
//            val data = AppDatabase.getInstance(this@M).favoriteDao().getFavorites()
//        }
    }

    private fun listGithub() {
        val rvGitHub = findViewById<RecyclerView>(R.id.rv_githubuser)
        adapter = CardViewGithubAdapter()
        rvGitHub.layoutManager = LinearLayoutManager(this)
        rvGitHub.adapter = adapter
    }

    private val mLoaderCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {
        lateinit var dataCursor: Cursor

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            val uri: Uri = Uri.parse("$SCHEME://$AUTHORITY/$TABLE_NAME")

            return when (id) {
                FAVORITE -> CursorLoader(applicationContext, uri, null, null, null, null)
                else -> throw IllegalArgumentException("Main Activity: Unknown URI")
            }
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            when (loader.id) {
                FAVORITE -> dataCursor = data as Cursor
                else -> throw IllegalArgumentException("Main Activity: Unknown loader id")
            }
            dataCursor.moveToFirst()
            val favoriteUsers = mutableListOf<GitHub>()
            while (!dataCursor.isAfterLast) {
                val fav = mapCursorToFavorite(dataCursor)
                if (fav != null) {
                    favoriteUsers.add(fav)
                }

                Log.d("mLoaderCallbacks", "$fav")
            }
            adapter.setUser(favoriteUsers)


//            if (!favoriteUsers.isNullOrEmpty()) {
//                adapter.set(favoriteUsers)
//                emptyListUI(false)
//            } else {
//                emptyListUI(true)
//            }
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
        }
    }
}