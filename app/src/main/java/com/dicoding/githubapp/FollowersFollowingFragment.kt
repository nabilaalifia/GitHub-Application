package com.dicoding.githubapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapp.databinding.FragmentFollowersFollowingBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersFollowingFragment : Fragment() {

    companion object {
        val EXTRA_URL = "extra_url"

        val TAG = "FollowersFollow"
    }

    private lateinit var adapter: CardViewGithubAdapter


    private lateinit var binding: FragmentFollowersFollowingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowersFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FollowersFollowing", "1")
        listGithub()
        Log.d("FollowersFollowing", arguments?.getString(EXTRA_URL).toString())
        getDataFromApi(arguments?.getString(EXTRA_URL) ?: "")
    }

    private fun getDataFromApi(url: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        Log.d("FollowersFollowing", "url : $url")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE
                // Parsing JSON
                val users = ArrayList<GitHub>()
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val items = JSONArray(result)

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val user = GitHub(
                            0, username, "", "", "", "", "", "", avatar, "", ""
                        )


                        users.add(user)

                    }
                    adapter.setUser(users)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun listGithub() {
        adapter = CardViewGithubAdapter()
        binding.rvGithubuser.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGithubuser.adapter = adapter
    }

}