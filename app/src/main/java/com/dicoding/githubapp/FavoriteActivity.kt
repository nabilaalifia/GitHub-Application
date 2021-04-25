package com.dicoding.githubapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: CardViewGithubAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val actionbar = supportActionBar
        actionbar?.title = "Favorite"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listGithub()
        getList()
    }

    private fun getList() {
        GlobalScope.launch {
            val data = AppDatabase.getInstance(this@FavoriteActivity).favoriteDao().getFavorites()

            withContext(Dispatchers.Main) {
                adapter.setUser(data)
            }
        }
    }

    private fun listGithub() {
        val rvGitHub = findViewById<RecyclerView>(R.id.rv_githubuser)
        adapter = CardViewGithubAdapter()
        rvGitHub.layoutManager = LinearLayoutManager(this)
        rvGitHub.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        getList()

        super.onResume()
    }
}