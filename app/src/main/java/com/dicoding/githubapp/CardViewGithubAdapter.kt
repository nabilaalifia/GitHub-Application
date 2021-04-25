package com.dicoding.githubapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class CardViewGithubAdapter :
    RecyclerView.Adapter<CardViewGithubAdapter.CardViewViewHolder>() {
    private var listGithub = mutableListOf<GitHub>()
    fun setUser(users: List<GitHub>) {
        listGithub.clear()
        listGithub.addAll(users)
        notifyDataSetChanged()
    }


    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cardview_github, parent, false)
        return CardViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        val github = listGithub[position]
        holder.tvName.text = github.username
        Glide.with(holder.itemView.context)
            .load(github.photo)
            .apply(RequestOptions())
            .into(holder.imgPhoto)
        val mContext = holder.itemView.context
        holder.itemView.setOnClickListener {
            val moveDetail = Intent(mContext, DetailGithubActivity::class.java)
            moveDetail.putExtra(DetailGithubActivity.EXTRA_DETAIL, github)
            mContext.startActivity(moveDetail)
        }
    }

    override fun getItemCount() = listGithub.size

}
