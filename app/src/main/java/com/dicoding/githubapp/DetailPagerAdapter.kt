package com.dicoding.githubapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubapp.FollowersFollowingFragment.Companion.EXTRA_URL

class DetailPagerAdapter(activity: AppCompatActivity, private val user: GitHub) :
    FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragmentFollowers = FollowersFollowingFragment()
                val args = Bundle()
                args.putString(EXTRA_URL, user.url_followers)
                fragmentFollowers.arguments = args
                fragmentFollowers
            }

            else -> {
                val fragmentFollowing = FollowersFollowingFragment()
                val args = Bundle()
                args.putString(EXTRA_URL, "https://api.github.com/users/${user.username}/following")
                fragmentFollowing.arguments = args
                fragmentFollowing
            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}