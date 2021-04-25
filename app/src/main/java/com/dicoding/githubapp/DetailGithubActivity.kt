package com.dicoding.githubapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubapp.databinding.ActivityDetailGithubBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class DetailGithubActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        val TAG = "DetailGithubActivity"
    }

    private lateinit var github: GitHub

    private lateinit var binding: ActivityDetailGithubBinding

    private val TAB_TITLES = intArrayOf(
        R.string.followers,
        R.string.following
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGithubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = "Detail User"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val getGithub = intent.getParcelableExtra<GitHub>(EXTRA_DETAIL)
        if (getGithub != null) {
            github = getGithub
        }
        Log.d(TAG, github.toString())
        getDataFromApi(github.username)

        checkFavorite(github)

        binding.btnFavorite.setOnClickListener {
            if (github.isFavorite) {
                removeFromFavorite(github)
                viewFavorite(false)
            } else {
                viewFavorite(true)
                addToFavorite(github)

            }
        }


    }

    private fun viewFavorite(favorited: Boolean) {
        if (!favorited) {
            binding.btnFavorite.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.cardview_dark_background
                )
            )
            binding.btnFavorite.text = "Like"
        } else {
            binding.btnFavorite.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.blueocean
                )
            )
            binding.btnFavorite.text = "Liked"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun getDataFromApi(username: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE
                // Parsing JSON
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseJsonObject = JSONObject(result)

                    val username = responseJsonObject.getString("login")
                    val avatar = responseJsonObject.getString("avatar_url")
                    val follower = responseJsonObject.getString("followers")
                    val following = responseJsonObject.getString("following")
                    val name = responseJsonObject.getString("name")
                    val id = responseJsonObject.getInt("id")
                    val following_url = responseJsonObject.getString("following_url")
                    val followers_url = responseJsonObject.getString("followers_url")
                    val user = GitHub(
                        id = id,
                        isFavorite = github.isFavorite,
                        username = username,
                        name = name,
                        photo = avatar,
                        followers = follower,
                        following = following,
                        url_followers = followers_url,
                        url_following = following_url,
                        location = responseJsonObject.getString("location"),
                        company = responseJsonObject.getString("company"),
                        repository = responseJsonObject.getString("public_repos")
                    )
//                    github.isFavorite = true
                    github = user
//                    viewFavorite(github.isFavorite)


                    Glide.with(this@DetailGithubActivity)
                        .load(user.photo)
                        .apply(RequestOptions())
                        .into(binding.imageView)

                    binding.tvItemName.text = user.name
                    binding.tUsername.text = user.username

                    binding.tvLocation.text = user.location
                    binding.tvCompany.text = user.company

                    binding.tvFollower.text = user.followers
                    binding.tvFollowing.text = user.following
                    binding.tvRepository.text = user.repository


                    val sectionsPagerAdapter =
                        user.let { DetailPagerAdapter(this@DetailGithubActivity, it) }
                    binding.viewPager.adapter = sectionsPagerAdapter
                    TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                        tab.text = resources.getString(TAB_TITLES[position])
                    }.attach()

                } catch (e: Exception) {
                    Toast.makeText(this@DetailGithubActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetailGithubActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun addToFavorite(github: GitHub) =
        CoroutineScope(GlobalScope.coroutineContext).launch {
            github.isFavorite = true
            AppDatabase.getInstance(this@DetailGithubActivity).favoriteDao().addFavorite(github)
        }

    fun removeFromFavorite(github: GitHub) =
        CoroutineScope(GlobalScope.coroutineContext).launch {
            github.isFavorite = false
            AppDatabase.getInstance(this@DetailGithubActivity).favoriteDao().removeFavorite(github)
        }

    fun checkFavorite(github: GitHub) =
        CoroutineScope(GlobalScope.coroutineContext).launch {
            val getGithub = AppDatabase.getInstance(this@DetailGithubActivity).favoriteDao()
                .getSingleFavorite(github.id)

            if (getGithub != null && getGithub.id != 0) {
                github.isFavorite = true
                viewFavorite(github.isFavorite)
            }
            Log.d(TAG, "getSingleFavorite $getGithub")

        }

}